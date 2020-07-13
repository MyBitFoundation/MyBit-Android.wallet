package com.wallet.crypto.mybitapp.ui.activity.splash;

import androidx.lifecycle.LiveData;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;

public abstract class SplashViewModel extends BaseViewModel {
    public abstract void prepare();

    public abstract LiveData<Wallet[]> wallets();

    public abstract LiveData<Boolean> onOpenPin();
}
