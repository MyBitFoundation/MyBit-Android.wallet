package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.ImportWalletInteract;
import com.wallet.crypto.mybitapp.interact.WalletValidationsInteract;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.ui.activity.importwallet.ImportWalletViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class ImportModule {
    @Provides
    ImportWalletViewModelFactory provideImportWalletViewModelFactory(
            ImportWalletInteract importWalletInteract,
            WalletValidationsInteract walletValidationsInteract,
            NetworkChangeReceiver networkChangeReceiver) {
        return new ImportWalletViewModelFactory(importWalletInteract, walletValidationsInteract, networkChangeReceiver);
    }

    @Provides
    ImportWalletInteract provideImportWalletInteract(
            WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        return new ImportWalletInteract(walletRepository, passwordStore);
    }

    @Provides
    WalletValidationsInteract provideWalletValidationsInteract() {
        return new WalletValidationsInteract();
    }
}
