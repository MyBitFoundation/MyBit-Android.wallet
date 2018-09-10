package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.WalletAlreadyExistsException;
import com.wallet.crypto.mybitapp.repository.entity.RealmWalletInfo;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Dasha on 15.06.2018
 */
public class RealmWalletInfoSource implements WalletInfoSource {

    @Override
    public Completable put(Wallet wallet) {
        return Completable.create(emitter -> {
            Realm realm = null;
            try {
                realm = getRealmInstance();
                RealmWalletInfo realmItem = realm.where(RealmWalletInfo.class)
                        .equalTo(RealmWalletInfo.COL_ADDRESS, wallet.address)
                        .and()
                        .equalTo(RealmWalletInfo.COL_TYPE, wallet.type)
                        .findFirst();

                if (realmItem != null) {
                    emitter.onError(new WalletAlreadyExistsException());
                    return;
                }

                realm.executeTransaction(r -> {
                    RealmWalletInfo obj = r.createObject(RealmWalletInfo.class, wallet.address);
                    obj.setType(wallet.type);
                });
                emitter.onComplete();
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    @Override
    public Single<Wallet[]> fetch() {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance();
                RealmResults<RealmWalletInfo> realmItems = realm.where(RealmWalletInfo.class).findAll();
                Wallet[] result = new Wallet[realmItems.size()];
                for (int i = 0; i < realmItems.size(); i++) {
                    RealmWalletInfo realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        Wallet wallet = new Wallet(
                                realmItem.getAddress(),
                                realmItem.getType()
                        );
                        result[i] = wallet;
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
    public Single<Boolean> hasAddress(String address) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance();
                RealmResults<RealmWalletInfo> realmItems = realm.where(RealmWalletInfo.class)
                        .equalTo(RealmWalletInfo.COL_ADDRESS, address)
                        .findAll();

                return realmItems.size() > 0;
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    @Override
    public Completable delete(Wallet wallet) {
        return Completable.fromAction(() -> {
            Realm realm = null;
            try {
                realm = getRealmInstance();
                realm.executeTransaction(r -> {
                    RealmResults<RealmWalletInfo> realmItems = r
                            .where(RealmWalletInfo.class)
                            .equalTo(RealmWalletInfo.COL_ADDRESS, wallet.address)
                            .and()
                            .equalTo(RealmWalletInfo.COL_TYPE, wallet.type)
                            .findAll();

                    realmItems.deleteAllFromRealm();
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

        });
    }

    private Realm getRealmInstance() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }
}
