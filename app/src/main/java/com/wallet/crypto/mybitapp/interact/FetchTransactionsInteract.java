package com.wallet.crypto.mybitapp.interact;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.repository.TransactionRepositoryType;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchTransactionsInteract {

    private final TransactionRepositoryType transactionRepository;

    public FetchTransactionsInteract(TransactionRepositoryType transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Observable<Transaction[]> fetch(Session session) {
        return transactionRepository
                .fetchTransactions(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Transaction[]> fetchBatch(Session session, int page) {
        return transactionRepository
                .fetchTransactions(session, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
