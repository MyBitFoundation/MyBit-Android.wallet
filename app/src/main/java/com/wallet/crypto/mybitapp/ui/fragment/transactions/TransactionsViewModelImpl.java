package com.wallet.crypto.mybitapp.ui.fragment.transactions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.interact.FetchTransactionsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.session.OnSessionChangeListener;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.TransactionDetailRouter;

public class TransactionsViewModelImpl extends TransactionsViewModel implements OnSessionChangeListener {

    private final MutableLiveData<Session> session = new MutableLiveData<>();
    private final MutableLiveData<Transaction[]> transactions = new MutableLiveData<>();
    private final MutableLiveData<Transaction[]> transactionsBatch = new MutableLiveData<>();

    private final GetSessionInteract getSessionInteract;
    private final FetchTransactionsInteract fetchTransactionsInteract;

    private final TransactionDetailRouter transactionDetailRouter;

    TransactionsViewModelImpl(
            GetSessionInteract getSessionInteract,
            FetchTransactionsInteract fetchTransactionsInteract,
            TransactionDetailRouter transactionDetailRouter,
            SessionRepositoryType sessionRepository) {
        this.getSessionInteract = getSessionInteract;
        this.fetchTransactionsInteract = fetchTransactionsInteract;
        this.transactionDetailRouter = transactionDetailRouter;
        sessionRepository.addOnSessionChangeListener(this);
    }

    @Override
    public LiveData<Session> session() {
        return session;
    }

    @Override
    public LiveData<Transaction[]> transactions() {
        return transactions;
    }

    @Override
    public LiveData<Transaction[]> transactionsBatch() {
        return transactionsBatch;
    }

    @Override
    public void prepare() {
        progress.postValue(true);
        compositeDisposable.add(getSessionInteract
                .get()
                .subscribe(this::onSession, this::onError));
    }

    private void onSession(Session session) {
        this.session.postValue(session);
        fetchTransactions(session);
    }

    @Override
    public void fetchNextTransactionsBatch(int page) {
        if (session.getValue() != null) {
            compositeDisposable.add(fetchTransactionsInteract
                    .fetchBatch(session.getValue(), page)
                    .subscribe(this.transactionsBatch::postValue, this::onError, this::onComplete));
        }
    }

    @Override
    public void fetchTransactions() {
        fetchTransactions(session.getValue());
    }

    private void fetchTransactions(Session session){
        if (session != null) {
            progress.postValue(true);
            compositeDisposable.clear();
            compositeDisposable.add(fetchTransactionsInteract
                    .fetch(session)
                    .subscribe(this.transactions::postValue, this::onError, this::onComplete));
        }
    }

    @Override
    protected void onError(Throwable throwable) {
        progress.postValue(false);
        super.onError(throwable);
    }

    private void onComplete() {
        progress.postValue(false);
    }

    @Override
    public void showDetails(Context context, Transaction transaction) {
        transactionDetailRouter.open(context, transaction);
    }

    @Override
    public void onSessionChanged(Session session) {
        this.progress.postValue(true);
        this.session.postValue(session);
        this.transactions.postValue(new Transaction[0]);
        fetchTransactions(session);
    }
}
