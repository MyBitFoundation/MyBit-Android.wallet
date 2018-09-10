package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ExternalBrowserRouter;
import com.wallet.crypto.mybitapp.router.ShareTransactionRouter;
import com.wallet.crypto.mybitapp.ui.activity.transactiondetail.TransactionDetailViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionDetailModule {

    @Provides
    TransactionDetailViewModelFactory provideTransactionDetailViewModelFactory(
            GetSessionInteract getSessionInteract,
            ExternalBrowserRouter externalBrowserRouter,
            ShareTransactionRouter shareTransactionRouter,
            NetworkChangeReceiver networkChangeReceiver) {
        return new TransactionDetailViewModelFactory(
                getSessionInteract, externalBrowserRouter, shareTransactionRouter, networkChangeReceiver);
    }

    @Provides
    ExternalBrowserRouter externalBrowserRouter() {
        return new ExternalBrowserRouter();
    }

    @Provides
    GetSessionInteract findDefaultWalletInteract(SessionRepositoryType sessionRepository, WalletRepositoryType walletRepository) {
        return new GetSessionInteract(sessionRepository, walletRepository);
    }

    @Provides
    ShareTransactionRouter provideShareTransactionRouter() {
        return new ShareTransactionRouter();
    }
}
