package com.wallet.crypto.mybitapp.ui.activity.importwallet;

import android.arch.lifecycle.LiveData;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.OnImportKeystoreListener;
import com.wallet.crypto.mybitapp.ui.widget.OnImportMnemonicListener;
import com.wallet.crypto.mybitapp.ui.widget.OnImportPrivateKeyListener;
import com.wallet.crypto.mybitapp.ui.widget.OnImportWatchAddressListener;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public abstract class ImportWalletViewModel extends NoInternetConnectionBaseViewModel implements
        OnImportKeystoreListener,
        OnImportPrivateKeyListener,
        OnImportMnemonicListener,
        OnImportWatchAddressListener {

    public ImportWalletViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract LiveData<Wallet> wallet();
}
