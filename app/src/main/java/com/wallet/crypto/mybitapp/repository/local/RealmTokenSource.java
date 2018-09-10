package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.entity.RealmTokenInfo;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmTokenSource implements TokenLocalSource {

    @Override
    public Completable put(NetworkInfo networkInfo, Wallet wallet, TokenInfo tokenInfo) {
        return Completable.fromAction(() -> putInNeed(networkInfo, wallet, tokenInfo));
    }

    @Override
    public Single<TokenInfo[]> fetch(NetworkInfo networkInfo, Wallet wallet) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance(networkInfo, wallet);
                RealmResults<RealmTokenInfo> realmItems = realm.where(RealmTokenInfo.class)
                        .sort("addedTime", Sort.ASCENDING)
                        .findAll();
                int len = realmItems.size();
                TokenInfo[] result = new TokenInfo[len];
                for (int i = 0; i < len; i++) {
                    RealmTokenInfo realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        result[i] = new TokenInfo(
                                realmItem.getAddress(),
                                realmItem.getName(),
                                realmItem.getSymbol(),
                                realmItem.getDecimals());
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

    private Realm getRealmInstance(NetworkInfo networkInfo, Wallet wallet) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(wallet.address + "-" + networkInfo.name + ".realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }

    private void putInNeed(NetworkInfo networkInfo, Wallet wallet, TokenInfo tokenInfo) {
        Realm realm = null;
        try {
            realm = getRealmInstance(networkInfo, wallet);
            RealmTokenInfo realmTokenInfo = realm.where(RealmTokenInfo.class)
                    .equalTo("symbol", tokenInfo.symbol.toUpperCase())
                    .findFirst();
            if (realmTokenInfo == null) {
                realm.executeTransaction(r -> {
                    RealmTokenInfo obj = r.createObject(RealmTokenInfo.class, tokenInfo.address);
                    obj.setName(tokenInfo.name);
                    obj.setSymbol(tokenInfo.symbol.toUpperCase());
                    obj.setDecimals(tokenInfo.decimals);
                    obj.setAddedTime(System.currentTimeMillis());
                });
            } else if (!realm.copyFromRealm(realmTokenInfo).getAddress().equalsIgnoreCase(tokenInfo.address)) {
                realm.executeTransaction(r -> {
                    RealmResults<RealmTokenInfo> results = r.where(RealmTokenInfo.class)
                            .equalTo("symbol", tokenInfo.symbol.toUpperCase())
                            .findAll();
                    results.deleteAllFromRealm();
                    RealmTokenInfo obj = r.createObject(RealmTokenInfo.class, tokenInfo.address);
                    obj.setName(tokenInfo.name);
                    obj.setSymbol(tokenInfo.symbol.toUpperCase());
                    obj.setDecimals(tokenInfo.decimals);
                    obj.setAddedTime(System.currentTimeMillis());
                });
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public Completable clear(NetworkInfo networkInfo, Wallet wallet) {
        return Completable.fromAction(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance(networkInfo, wallet);
                realm.executeTransaction(r -> {
                    RealmResults<RealmTokenInfo> results = r.where(RealmTokenInfo.class).findAll();
                    results.deleteAllFromRealm();
                });

            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }
}
