package com.wallet.crypto.mybitapp.ui.fragment.transactions;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.FetchTransactionsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.TransactionDetailRouter;

public class TransactionsViewModelFactory implements ViewModelProvider.Factory {

    private final GetSessionInteract getSessionInteract;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final TransactionDetailRouter transactionDetailRouter;

    private final SessionRepositoryType sessionRepository;

    public TransactionsViewModelFactory(
            GetSessionInteract getSessionInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            TransactionDetailRouter transactionDetailRouter,
            SessionRepositoryType sessionRepository) {
        this.getSessionInteract = getSessionInteract;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.transactionDetailRouter = transactionDetailRouter;
        this.sessionRepository = sessionRepository;
    }

    @NonNull
    @Override
    public TransactionsViewModel create(@NonNull Class modelClass) {
        return new TransactionsViewModelImpl(
                getSessionInteract,
                fetchTransactionsInteract,
                transactionDetailRouter,
                sessionRepository);
    }
}
