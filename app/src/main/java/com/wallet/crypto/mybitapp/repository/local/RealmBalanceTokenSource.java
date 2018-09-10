package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.entity.RealmBalanceToken;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Dasha on 14.06.2018
 */
public class RealmBalanceTokenSource implements BalanceTokenSource {

    @Override
    public Completable put(NetworkInfo networkInfo, Wallet wallet, Balance balance, String tokenAddress) {
        return Completable.fromAction(() -> insertOrUpdate(networkInfo, wallet, balance, tokenAddress));
    }

    private void insertOrUpdate(NetworkInfo networkInfo, Wallet wallet, Balance balance, String tokenAddress) {
        Realm realm = null;
        try {
            realm = getRealmInstance(networkInfo, wallet);
            realm.executeTransaction(r -> {
                RealmBalanceToken realmBalanceToken = new RealmBalanceToken(
                        tokenAddress,
                        balance.cryptoBalance.toPlainString(),
                        balance.usdPrice.toPlainString(),
                        balance.percentChange24h.toPlainString()
                );

                r.insertOrUpdate(realmBalanceToken);
            });

        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public Single<Map<String, Balance>> fetch(NetworkInfo networkInfo, Wallet wallet) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance(networkInfo, wallet);
                RealmResults<RealmBalanceToken> realmItems = realm.where(RealmBalanceToken.class).findAll();
                Map<String, Balance> result = new HashMap<>();
                for (int i = 0; i < realmItems.size(); i++) {
                    RealmBalanceToken realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        Balance balance = new Balance(
                                new BigDecimal(realmItem.getCryptoBalance()),
                                new BigDecimal(realmItem.getUsdPrice()),
                                new BigDecimal(realmItem.getPercentChange24h())
                        );
                        result.put(realmItem.getTokenAddress(), balance);
                    }
                }
                return result;
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    @Override
    public Completable clear(NetworkInfo networkInfo, Wallet wallet) {
        return Completable.fromAction(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance(networkInfo, wallet);
                realm.executeTransaction(r -> {
                    RealmResults<RealmBalanceToken> realmItems = r.where(RealmBalanceToken.class).findAll();
                    realmItems.deleteAllFromRealm();
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    private Realm getRealmInstance(NetworkInfo networkInfo, Wallet wallet) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(wallet.address + "-" + networkInfo.name + ".realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }

}
