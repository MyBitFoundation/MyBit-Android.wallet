package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.FetchTransactionsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ManageWalletsRouter;
import com.wallet.crypto.mybitapp.router.MyTokensRouter;
import com.wallet.crypto.mybitapp.router.ReceiveRouter;
import com.wallet.crypto.mybitapp.router.SendRouter;
import com.wallet.crypto.mybitapp.router.TransactionDetailRouter;
import com.wallet.crypto.mybitapp.ui.fragment.transactions.TransactionsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class TransactionsFragmentModule {
    @Provides
    public TransactionsViewModelFactory provideTransactionsViewModelFactory(
            GetSessionInteract getSessionInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            TransactionDetailRouter transactionDetailRouter,
            SessionRepositoryType sessionRepository) {
        return new TransactionsViewModelFactory(
                getSessionInteract,
                fetchTransactionsInteract,
                transactionDetailRouter,
                sessionRepository);
    }

    @Provides
    FetchTransactionsInteract provideFetchTransactionsInteract(TransactionRepositoryType transactionRepository) {
        return new FetchTransactionsInteract(transactionRepository);
    }

    @Provides
    ManageWalletsRouter provideManageWalletsRouter() {
        return new ManageWalletsRouter();
    }

    @Provides
    SendRouter provideSendRouter() {
        return new SendRouter();
    }

    @Provides
    TransactionDetailRouter provideTransactionDetailRouter() {
        return new TransactionDetailRouter();
    }

    @Provides
    ReceiveRouter provideMyAddressRouter() {
        return new ReceiveRouter();
    }

    @Provides
    MyTokensRouter provideMyTokensRouter() {
        return new MyTokensRouter();
    }

    @Provides
    GetSessionInteract provideGetSessionInteract(SessionRepositoryType sessionRepository, WalletRepositoryType walletRepository) {
        return new GetSessionInteract(sessionRepository, walletRepository);
    }

}
