package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;

import io.reactivex.Single;

public class TransactionInDiskSource implements TransactionLocalSource {

    @Override
    public Single<Transaction[]> fetchTransaction(Session session, int page) {
        return Single.just(new Transaction[0]);
    }

    @Override
    public void putTransactions(Session session, Transaction[] transactions, int page) {
    }

    @Override
    public void clear() {
    }
}
