package com.wallet.crypto.mybitapp.ui.activity.splash;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.wallet.crypto.mybitapp.BuildConfig;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.router.MainRouter;
import com.wallet.crypto.mybitapp.router.ManageWalletsRouter;
import com.wallet.crypto.mybitapp.ui.base.BaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity<SplashViewModel> {
    @Inject
    SplashViewModelFactory splashViewModelFactory;

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, splashViewModelFactory)
                .get(SplashViewModel.class);
        viewModel.wallets().observe(this, this::onWallets);
        viewModel.onOpenPin().observe(this, this::onOpenPin);
    }

    @Override
    protected void onRemovedPinFragment() { }

    private void onOpenPin(Boolean checked) {
        showPin(true, true, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.prepare();
    }

    private void onWallets(Wallet[] wallets) {
        if (wallets.length == 0) {
            new ManageWalletsRouter().open(this);
            finishAffinity();
        } else {
            new MainRouter().open(this, true);
        }
    }
}