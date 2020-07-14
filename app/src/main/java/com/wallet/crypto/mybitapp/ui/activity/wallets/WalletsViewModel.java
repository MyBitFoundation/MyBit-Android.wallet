package com.wallet.crypto.mybitapp.ui.activity.wallets;

import android.app.Activity;
import androidx.lifecycle.LiveData;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public abstract class WalletsViewModel extends NoInternetConnectionBaseViewModel {
    public WalletsViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract void setDefaultWallet(Wallet wallet);

    public abstract void deleteWallet(Wallet wallet);

    public abstract void fetchWallets();

    public abstract void newWallet();

    public abstract void exportWallet(Wallet wallet, String storePassword);

    public abstract void importWallet(Activity activity);

    public abstract void showMain(Activity activity);

    public abstract LiveData<Wallet[]> wallets();

    public abstract LiveData<Object> noWallets();

    public abstract LiveData<Wallet> defaultWallet();

    public abstract LiveData<Wallet> createdWallet();

    public abstract LiveData<String> exportedStore();
}
