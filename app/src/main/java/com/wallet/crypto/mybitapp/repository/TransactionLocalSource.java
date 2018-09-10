package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.Wallet;

import io.reactivex.Single;

public interface TransactionLocalSource {
    Single<Transaction[]> fetchTransaction(Session session, int page);

    void putTransactions(Session session, Transaction[] transactions, int page);

    void clear();
}
