package com.wallet.crypto.mybitapp.service;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;

import java.util.List;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
	Observable<Transaction[]> fetchTransactions(Session session, int page);
}
