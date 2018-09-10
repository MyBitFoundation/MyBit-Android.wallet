package com.wallet.crypto.mybitapp.ui.activity.send;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.barcode.BarcodeCaptureActivity;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;
import com.wallet.crypto.mybitapp.util.QRURLParser;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class SendActivity extends NoInternetConnectionBaseActivity<SendViewModel> {
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    private static final String STRING_FORMAT = "%s %s";

    @Inject
    SendViewModelFactory sendViewModelFactory;

    private BigDecimal balance;
    private boolean sendingTokens = false;
    private String contractAddress;
    private int decimals;
    private String symbol;

    @BindView(R.id.to_input_layout)
    TextInputLayout toInputLayout;

    @BindView(R.id.amount_input_layout)
    TextInputLayout amountInputLayout;

    @BindView(R.id.send_to_address)
    EditText toAddressText;

    @BindView(R.id.send_amount)
    EditText amountText;

    @OnClick(R.id.scan_barcode_button)
    void onScanBarcodeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_send;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
        initIntents();
        extractStrings();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, sendViewModelFactory)
                .get(SendViewModel.class);
        viewModel.setValidAddress().observe(this, this::onValidAddress);
        viewModel.setValidAmount().observe(this, this::onValidAmount);
        viewModel.setErrorInvalidAddress().observe(this, this::onSetErrorInvalidAddress);
        viewModel.setErrorInvalidAmount().observe(this, this::onSetErrorInvalidAmount);
        viewModel.setErrorAmountPrecisionEth().observe(this, this::onSetErrorAmountPrecisionEth);
        viewModel.setErrorAmountPrecisionMybit().observe(this, this::onSetErrorAmountPrecisionMybit);
    }

    private void initIntents() {
        contractAddress = getIntent().getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        decimals = getIntent().getIntExtra(C.EXTRA_DECIMALS, C.ETHER_DECIMALS);
        symbol = getIntent().getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.ETH_SYMBOL : symbol;
        sendingTokens = getIntent().getBooleanExtra(C.EXTRA_SENDING_TOKENS, false);
        balance = getBigDecimalExtra(getIntent(), C.EXTRA_BALANCE);
    }

    private BigDecimal getBigDecimalExtra(Intent intent, String key) {
        try {
            return new BigDecimal(intent.getStringExtra(key));
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    private void extractStrings() {
        setTitle(String.format(STRING_FORMAT, getString(R.string.title_send), symbol));
        amountInputLayout.setHint(String.format(STRING_FORMAT, getString(R.string.hint_amount), symbol));
        String toAddress = getIntent().getStringExtra(C.EXTRA_ADDRESS);
        trySetAddress(toAddress);
    }

    private void trySetAddress(String toAddress) {
        if (toAddress != null) {
            toAddressText.setText(toAddress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next: {
                onNext();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String qrCodeResult = data.getStringExtra(BarcodeCaptureActivity.QR_CODE_RESULT);
                    QRURLParser parser = QRURLParser.getInstance();
                    String extractedAddress = parser.extractAddressFromQrString(qrCodeResult);
                    checkExtractedAddress(extractedAddress, qrCodeResult);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkExtractedAddress(String extractedAddress, String qrCodeResult) {
        if (extractedAddress == null) {
            Toast.makeText(this, R.string.toast_qr_code_no_address, Toast.LENGTH_SHORT).show();
        } else {
            toAddressText.setText(qrCodeResult);
        }
    }

    private void onValidAddress(Object object) {
        toInputLayout.setErrorEnabled(false);
    }

    private void onValidAmount(Object object) {
        amountInputLayout.setErrorEnabled(false);
    }

    private void onSetErrorInvalidAddress(Object object) {
        toInputLayout.setError(getString(R.string.error_invalid_address));
    }

    private void onSetErrorInvalidAmount(Object object) {
        amountInputLayout.setError(getString(R.string.error_invalid_amount));
    }

    private void onSetErrorInsufficientFunds(Object object) {
        amountInputLayout.setError(getString(R.string.error_insufficient_funds));
    }

    private void onSetErrorAmountPrecisionEth(Object object) {
        amountInputLayout.setError(getString(R.string.error_invalid_amount_precision_eth));
    }

    private void onSetErrorAmountPrecisionMybit(Object object) {
        amountInputLayout.setError(getString(R.string.error_invalid_amount_precision_myb));
    }

    private void onNext() {
        final String to = toAddressText.getText().toString();
        final String amount = amountText.getText().toString();
        viewModel.tryOpenConfirmation(this, to, amount, decimals, contractAddress, symbol, sendingTokens, balance);
    }
}