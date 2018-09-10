package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.repository.entity.RealmPassCode;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmPassCodeSource implements PassCodeSource {
    @Override
    public Completable put(String passCodeHash, Boolean isOptioned) {
        return Completable.fromAction(() -> insertOrUpdate(passCodeHash, isOptioned));
    }

    private void insertOrUpdate(String passCodeHash, Boolean isOptioned) {
        Realm realm = null;
        try {
            realm = getRealmInstance();
            realm.executeTransaction(t -> {
                RealmPassCode realmPassCode = new RealmPassCode(passCodeHash, isOptioned);
                t.insertOrUpdate(realmPassCode);
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public Single<Map<String, Boolean>> fetch() {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance();
                RealmResults<RealmPassCode> realmItems = realm.where(RealmPassCode.class).findAll();
                Map<String, Boolean> result = new HashMap<>();
                for (int i = 0; i < realmItems.size(); i++) {
                    RealmPassCode realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        result.put(realmItem.getPassCodeHash(), realmItem.getOptioned());
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
    public Completable remove() {
        return Completable.fromAction(this::cleanPassCodeHashData);
    }

    private void cleanPassCodeHashData() {
        Realm realm = null;
        try {
            realm = getRealmInstance();
            realm.executeTransaction(t -> {
                t.deleteAll();
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private Realm getRealmInstance() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("passcodehash.realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }
}