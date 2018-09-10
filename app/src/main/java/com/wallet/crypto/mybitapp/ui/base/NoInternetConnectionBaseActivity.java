package com.wallet.crypto.mybitapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class NoInternetConnectionBaseActivity<T extends NoInternetConnectionBaseViewModel> extends BaseActivity<T> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPostPrepareViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestChangedNetworkConnection();
    }

    @Override
    protected void onRemovedPinFragment() {
        requestChangedNetworkConnection();
    }

    private void requestChangedNetworkConnection() {
        viewModel.requestForChangingNetworkConnection(this);
    }

    private void onPostPrepareViewModel() {
        viewModel.onNetworkConnectionChanged().observe(this, this::onNetworkConnectionChanged);
    }

    private void onNetworkConnectionChanged(boolean isNetworkOn) {
        if (isNetworkOn) {
            hideNotInternetConnection();
        } else {
            showNotInternetConnection();
        }
    }
}
