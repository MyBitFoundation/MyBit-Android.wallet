package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.Wallet;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TransactionRepositoryType {

    Observable<Transaction[]> fetchTransactions(Session session);

    Observable<Transaction[]> fetchTransactions(Session session, int page);

    Single<String> createTransaction(Wallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password);

    Completable clearPending();
}
