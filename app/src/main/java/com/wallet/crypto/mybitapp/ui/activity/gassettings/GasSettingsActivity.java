package com.wallet.crypto.mybitapp.ui.activity.gassettings;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;
import com.wallet.crypto.mybitapp.util.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

public class GasSettingsActivity extends NoInternetConnectionBaseActivity<GasSettingsViewModel> {

    @Inject
    GasSettingsViewModelFactory viewModelFactory;

    @BindView(R.id.gas_price_text)
    TextView gasPriceText;

    @BindView(R.id.gas_limit_text)
    TextView gasLimitText;

    @BindView(R.id.text_network_fee)
    TextView networkFeeText;

    @BindView(R.id.gas_price_info_text)
    TextView gasPriceInfoText;

    @BindView(R.id.gas_limit_info_text)
    TextView gasLimitInfoText;

    @BindView(R.id.gas_price_slider)
    SeekBar gasPriceSlider;

    @BindView(R.id.gas_limit_slider)
    SeekBar gasLimitSlider;

    @Override
    protected int getContentView() {
        return R.layout.activity_gas_settings;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(GasSettingsViewModel.class);

        gasPriceSlider.setPadding(0, 0, 0, 0);
        gasLimitSlider.setPadding(0, 0, 0, 0);

        BigInteger gasPrice = new BigInteger(getIntent().getStringExtra(C.EXTRA_GAS_PRICE));
        BigInteger gasLimit = new BigInteger(getIntent().getStringExtra(C.EXTRA_GAS_LIMIT));
        BigInteger gasLimitMin = BigInteger.valueOf(C.GAS_LIMIT_MIN);
        BigInteger gasLimitMax = BigInteger.valueOf(C.GAS_LIMIT_MAX);
        BigInteger gasPriceMin = BigInteger.valueOf(C.GAS_PRICE_MIN);
        BigInteger networkFeeMax = BigInteger.valueOf(C.NETWORK_FEE_MAX);

        final int gasPriceMinGwei = BalanceUtils.weiToGweiBI(gasPriceMin).intValue();
        gasPriceSlider.setMax(BalanceUtils
                .weiToGweiBI(networkFeeMax.divide(gasLimitMax))
                .subtract(BigDecimal.valueOf(gasPriceMinGwei))
                .intValue());
        int gasPriceProgress = BalanceUtils
                .weiToGweiBI(gasPrice)
                .subtract(BigDecimal.valueOf(gasPriceMinGwei))
                .intValue();
        gasPriceSlider.setProgress(gasPriceProgress);
        gasPriceSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        viewModel.gasPrice().setValue(BalanceUtils.gweiToWei(BigDecimal.valueOf(progress + gasPriceMinGwei)));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        gasLimitSlider.setMax(gasLimitMax.subtract(gasLimitMin).intValue());
        gasLimitSlider.setProgress(gasLimit.subtract(gasLimitMin).intValue());
        gasLimitSlider.refreshDrawableState();
        gasLimitSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress = progress / 100;
                        progress = progress * 100;
                        viewModel.gasLimit().setValue(BigInteger.valueOf(progress).add(gasLimitMin));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        viewModel.gasPrice().observe(this, this::onGasPrice);
        viewModel.gasLimit().observe(this, this::onGasLimit);
        viewModel.defaultNetwork().observe(this, this::onDefaultNetwork);
        viewModel.gasPrice().setValue(gasPrice);
        viewModel.gasLimit().setValue(gasLimit);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.prepare();
    }

    private void onDefaultNetwork(NetworkInfo network) {
        gasPriceInfoText.setText(getString(R.string.info_gas_price, network.name));
        gasLimitInfoText.setText(getString(R.string.info_gas_limit, network.symbol));
    }

    private void onGasPrice(BigInteger price) {
        String priceStr = BalanceUtils.weiToGwei(price) + " " + C.GWEI_UNIT;
        gasPriceText.setText(priceStr);

        updateNetworkFee();
    }

    private void onGasLimit(BigInteger limit) {
        gasLimitText.setText(limit.toString());

        updateNetworkFee();
    }

    private void updateNetworkFee() {
        String fee = BalanceUtils.weiToEth(viewModel.networkFee()).toPlainString() + " " + C.ETH_SYMBOL;
        networkFeeText.setText(fee);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_settings_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                Intent intent = new Intent();
                intent.putExtra(C.EXTRA_GAS_PRICE, viewModel.gasPrice().getValue().toString());
                intent.putExtra(C.EXTRA_GAS_LIMIT, viewModel.gasLimit().getValue().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
