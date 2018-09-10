package com.wallet.crypto.mybitapp.repository;

import android.text.format.DateUtils;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;

import java.util.Map;

import io.reactivex.Single;

public class TransactionInMemorySource implements TransactionLocalSource {

    private static final long MAX_TIME_OUT = DateUtils.MINUTE_IN_MILLIS;
    private final Map<String, CacheUnit> cache = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public Single<Transaction[]> fetchTransaction(Session session, int page) {
        return Single.fromCallable(() -> {
            String key = getKey(session, page);
            CacheUnit unit = cache.get(key);
            Transaction[] transactions = new Transaction[0];
            if (unit != null) {
                if (System.currentTimeMillis() - unit.create > MAX_TIME_OUT) {
                    clearBatches(getKey(session));
                } else {
                    transactions = unit.transactions;
                }
            }

            return transactions;
        });
    }

    @Override
    public void putTransactions(Session session, Transaction[] transactions, int page) {
        cache.put(getKey(session, page), new CacheUnit(session.wallet.address, System.currentTimeMillis(), transactions));
    }

    private String getKey(Session session) {
        return session.wallet.address + "-"
                + session.wallet.type + "-"
                + session.networkInfo.name;
    }

    private String getKey(Session session, int page) {
        return session.wallet.address + "-"
                + session.wallet.type + "-"
                + session.networkInfo.name + "-" + page;
    }

    private void clearBatches(String startKey) {
        for (String key : cache.keySet()) {
            if (key.startsWith(startKey)) {
                cache.remove(key);
            }
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }

    private static class CacheUnit {
        final String accountAddress;
        final long create;

        final Transaction[] transactions;

        private CacheUnit(String accountAddress, long create, Transaction[] transactions) {
            this.accountAddress = accountAddress;
            this.create = create;
            this.transactions = transactions;
        }
    }
}
