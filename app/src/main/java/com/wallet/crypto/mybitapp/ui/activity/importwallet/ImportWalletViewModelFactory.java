package com.wallet.crypto.mybitapp.ui.activity.importwallet;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.ImportWalletInteract;
import com.wallet.crypto.mybitapp.interact.WalletValidationsInteract;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class ImportWalletViewModelFactory implements ViewModelProvider.Factory {

    private final ImportWalletInteract importWalletInteract;
    private final WalletValidationsInteract walletValidationsInteract;
    private final NetworkChangeReceiver networkChangeReceiver;

    public ImportWalletViewModelFactory(ImportWalletInteract importWalletInteract, WalletValidationsInteract walletValidationsInteract,
                                        NetworkChangeReceiver networkChangeReceiver) {
        this.importWalletInteract = importWalletInteract;
        this.walletValidationsInteract = walletValidationsInteract;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public ImportWalletViewModel create(@NonNull Class modelClass) {
        return new ImportWalletViewModelImpl(importWalletInteract, walletValidationsInteract, networkChangeReceiver);
    }
}
