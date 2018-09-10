package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.CreateWalletInteract;
import com.wallet.crypto.mybitapp.interact.DefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.DeleteWalletInteract;
import com.wallet.crypto.mybitapp.interact.ExportWalletInteract;
import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ImportWalletRouter;
import com.wallet.crypto.mybitapp.router.MainRouter;
import com.wallet.crypto.mybitapp.ui.activity.wallets.WalletsViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class AccountsManageModule {

    @Provides
    WalletsViewModelFactory provideAccountsManageViewModelFactory(
            CreateWalletInteract createWalletInteract,
            DefaultWalletInteract defaultWalletInteract,
            DeleteWalletInteract deleteWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExportWalletInteract exportWalletInteract,
            ImportWalletRouter importWalletRouter,
            MainRouter mainRouter,
            PassCodeInteract passCodeInteract,
            NetworkChangeReceiver networkChangeReceiver) {
        return new WalletsViewModelFactory(createWalletInteract,
                defaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
                mainRouter,
                passCodeInteract,
                networkChangeReceiver);
    }

    @Provides
    CreateWalletInteract provideCreateAccountInteract(
            WalletRepositoryType accountRepository, PasswordStore passwordStore) {
        return new CreateWalletInteract(accountRepository, passwordStore);
    }

    @Provides
    DefaultWalletInteract provideSetDefaultAccountInteract(SessionRepositoryType sessionRepository, TransactionRepositoryType transactionRepository) {
        return new DefaultWalletInteract(sessionRepository, transactionRepository);
    }

    @Provides
    DeleteWalletInteract provideDeleteAccountInteract(
            WalletRepositoryType accountRepository, PasswordStore store) {
        return new DeleteWalletInteract(accountRepository, store);
    }

    @Provides
    FetchWalletsInteract provideFetchAccountsInteract(WalletRepositoryType accountRepository) {
        return new FetchWalletsInteract(accountRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultAccountInteract(WalletRepositoryType accountRepository) {
        return new FindDefaultWalletInteract(accountRepository);
    }

    @Provides
    ExportWalletInteract provideExportWalletInteract(
            WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        return new ExportWalletInteract(walletRepository, passwordStore);
    }

    @Provides
    ImportWalletRouter provideImportAccountRouter() {
        return new ImportWalletRouter();
    }

    @Provides
    MainRouter provideMainRouter() {
        return new MainRouter();
    }
}
