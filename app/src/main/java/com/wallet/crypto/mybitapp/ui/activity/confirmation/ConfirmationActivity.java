package com.wallet.crypto.mybitapp.ui.activity.confirmation;

import androidx.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.GasModel;
import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.ui.activity.gassettings.GasSettingsViewModelImpl;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;
import com.wallet.crypto.mybitapp.util.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ConfirmationActivity extends NoInternetConnectionBaseActivity<ConfirmationViewModel> {
    AlertDialog dialog;

    @Inject
    ConfirmationViewModelFactory confirmationViewModelFactory;

    private BigDecimal balance;
    private BigInteger amount;
    private int decimals;
    private String contractAddress;
    private String symbol;
    private String toAddress;
    private boolean confirmationForTokenTransfer = false;

    @BindView(R.id.text_from)
    TextView fromAddressText;

    @BindView(R.id.text_to)
    TextView toAddressText;

    @BindView(R.id.text_value)
    TextView valueText;

    @BindView(R.id.text_gas_price)
    TextView gasPriceText;

    @BindView(R.id.text_gas_limit)
    TextView gasLimitText;

    @BindView(R.id.text_network_fee)
    TextView networkFeeText;

    @OnClick(R.id.send_button)
    void onSendButtonClicked() {
        onSend();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_confirm;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
        extractIntents();
        initStrings();
    }

    private void extractIntents() {
        toAddress = getIntent().getStringExtra(C.EXTRA_TO_ADDRESS);
        contractAddress = getIntent().getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        amount = getBigIntegerExtra(getIntent(), C.EXTRA_AMOUNT);
        decimals = getIntent().getIntExtra(C.EXTRA_DECIMALS, -1);
        symbol = getIntent().getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.ETH_SYMBOL : symbol;
        confirmationForTokenTransfer = !symbol.equalsIgnoreCase(C.ETH_SYMBOL);
        balance = getBigDecimalExtra(getIntent(), C.EXTRA_BALANCE);
    }

    private BigInteger getBigIntegerExtra(Intent intent, String key) {
        try {
            return new BigInteger(intent.getStringExtra(key));
        } catch (Exception ex) {
            return BigInteger.ZERO;
        }
    }

    private BigDecimal getBigDecimalExtra(Intent intent, String key) {
        try {
            return new BigDecimal(intent.getStringExtra(key));
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    private void initStrings() {
        toAddressText.setText(toAddress);
        String amountString = "-" + BalanceUtils.subunitToBase(amount, decimals).toPlainString() + " " + symbol;
        valueText.setText(amountString);
        valueText.setTextColor(ContextCompat.getColor(this, R.color.red));
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory)
                .get(ConfirmationViewModel.class);
        viewModel.session().observe(this, this::onSession);
        viewModel.gasModel().observe(this, this::onGasSettings);
        viewModel.sendTransaction().observe(this, this::onTransaction);
        viewModel.progress().observe(this, this::onProgress);
        viewModel.error().observe(this, this::onError);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirmation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                viewModel.openGasSettings(ConfirmationActivity.this);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.prepare(confirmationForTokenTransfer);
    }

    private void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_sending)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void onSend() {
        viewModel.sendAmount(
                fromAddressText.getText().toString(),
                toAddressText.getText().toString(),
                contractAddress,
                amount,
                balance);
    }

    private void onSession(Session session) {
        fromAddressText.setText(session.wallet.address);
    }

    private void onTransaction(String hash) {
        hideDialog();
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.transaction_succeeded)
                .setMessage(hash)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    finish();
                })
                .setNeutralButton(R.string.copy, (dialog1, id) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("transaction hash", hash);
                    clipboard.setPrimaryClip(clip);
                    finish();
                })
                .create();
        dialog.show();
    }

    private void onGasSettings(GasModel gasModel) {
        String gasPrice = String.format("%1$s %2$s", gasModel.gasPrice, C.GWEI_UNIT);
        String networkFee = String.format("%1$s %2$s", gasModel.networkFee, C.ETH_SYMBOL);
        gasPriceText.setText(gasPrice);
        gasLimitText.setText(gasModel.gasLimit);
        networkFeeText.setText(networkFee);
    }

    private void onError(ErrorEnvelope error) {
        if (error.code == C.ErrorCode.INSUFFICIENT_FUNDS) {
            onError(getString(R.string.error_insufficient_funds));
        } else {
            onError(getString(R.string.error_transaction_failed));
        }
    }

    private void onError(String message) {
        hideDialog();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_error)
                .setMessage(message)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    // Do nothing
                })
                .create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == GasSettingsViewModelImpl.SET_GAS_SETTINGS) {
            if (resultCode == RESULT_OK) {
                BigInteger gasPrice = new BigInteger(intent.getStringExtra(C.EXTRA_GAS_PRICE));
                BigInteger gasLimit = new BigInteger(intent.getStringExtra(C.EXTRA_GAS_LIMIT));
                GasSettings settings = new GasSettings(gasPrice, gasLimit);
                viewModel.gasSettings().postValue(settings);
            }
        }
    }
}
