package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.CreateTransactionInteract;
import com.wallet.crypto.mybitapp.interact.FetchGasSettingsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.PreferenceRepositoryType;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.GasSettingsRouter;
import com.wallet.crypto.mybitapp.ui.activity.confirmation.ConfirmationViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfirmationModule {
    @Provides
    public ConfirmationViewModelFactory provideConfirmationViewModelFactory(
            GetSessionInteract getSessionInteract,
            FetchGasSettingsInteract fetchGasSettingsInteract,
            CreateTransactionInteract createTransactionInteract,
            GasSettingsRouter gasSettingsRouter,
            NetworkChangeReceiver networkChangeReceiver) {
        return new ConfirmationViewModelFactory(getSessionInteract, fetchGasSettingsInteract, createTransactionInteract, gasSettingsRouter, networkChangeReceiver);
    }

    @Provides
    GetSessionInteract provideGetSessionInteract(SessionRepositoryType sessionRepository, WalletRepositoryType walletRepository) {
        return new GetSessionInteract(sessionRepository, walletRepository);
    }

    @Provides
    FetchGasSettingsInteract provideFetchGasSettingsInteract(PreferenceRepositoryType preferenceRepositoryType) {
        return new FetchGasSettingsInteract(preferenceRepositoryType);
    }

    @Provides
    CreateTransactionInteract provideCreateTransactionInteract(TransactionRepositoryType transactionRepository, PasswordStore passwordStore) {
        return new CreateTransactionInteract(transactionRepository, passwordStore);
    }

    @Provides
    GasSettingsRouter provideGasSettingsRouter() {
        return new GasSettingsRouter();
    }
}
