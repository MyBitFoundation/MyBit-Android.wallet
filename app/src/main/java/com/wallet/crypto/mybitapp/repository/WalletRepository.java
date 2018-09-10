package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.wallet.WalletDataSourceType;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class WalletRepository implements WalletRepositoryType {

    private final PreferenceRepositoryType preferenceRepositoryType;
    private final WalletDataSourceType[] dataSources;
    private final int defaultType;

    public WalletRepository(int defaultType, PreferenceRepositoryType preferenceRepositoryType,
                            WalletDataSourceType... dataSources) {
        this.preferenceRepositoryType = preferenceRepositoryType;
        this.dataSources = dataSources;
        this.defaultType = defaultType;
    }

    @Override
    public Single<Wallet[]> fetchWallets() {
        return Single.defer(() -> {
            Map<String, Wallet> wallets = new HashMap<>();

            for (WalletDataSourceType dataSource : dataSources) {
                Wallet[] dataSourceWallets = dataSource.fetchAll().blockingGet();

                for (Wallet wallet : dataSourceWallets) {
                    wallets.put(wallet.address.concat(String.valueOf(wallet.type)), wallet);
                }
            }

            return Single.just(wallets.values().toArray(new Wallet[wallets.values().size()]));
        });
    }

    @Override
    public Single<Wallet> findWallet(String address) {
        return fetchWallets()
                .flatMap(accounts -> {
                    for (Wallet wallet : accounts) {
                        if (wallet.sameAddress(address)) {
                            return Single.just(wallet);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Single<Wallet> createWallet(String password) {
        return getDataSource(defaultType).flatMap(dataSource -> dataSource.create(password));
    }

    @Override
    public Single<Wallet> addWallet(int type, String value, String password, String newPassword) {
        return getDataSource(type)
                .flatMap(dataSource -> dataSource.add(value, password, newPassword, type));
    }

    @Override
    public Single<String> exportWallet(Wallet wallet, String password, String newPassword) {
        return getDataSource(wallet).flatMap(dataSource -> dataSource.export(wallet, password, newPassword));
    }

    @Override
    public Single<Wallet[]> deleteWallet(Wallet wallet, String password) {
        return getDataSource(wallet)
                .subscribeOn(Schedulers.io())
                .flatMap(dataSource -> dataSource
                        .delete(wallet, password)
                        .andThen(fetchWallets()))
                .observeOn(Schedulers.io())
                .flatMap(wallets -> {
                    preferenceRepositoryType.clearDefaultWallet();
                    return Single.just(wallets);
                });
    }

    @Override
    public Single<Wallet> removeWatchedDuplicateWallet(Wallet wallet) {
        return getDataSource(Wallet.WATCH)
                .flatMapCompletable(dataSource ->
                        dataSource.delete(new Wallet(wallet.address, Wallet.WATCH), null)
                )
                .toSingle(() -> {
                    preferenceRepositoryType.clearDefaultWallet();
                    return wallet;
                });
    }

    @Override
    public Completable setDefaultWallet(Wallet wallet) {
        return Completable.fromAction(() -> preferenceRepositoryType.setDefaultWallet(wallet));
    }

    @Override
    public Single<Wallet> getDefaultWallet() {
        return Single.fromCallable(preferenceRepositoryType::getDefaultWallet);
    }

    private Single<WalletDataSourceType> getDataSource(Wallet wallet) {
        if (wallet.type == -1) {
            return getDataSource(wallet.address);
        }
        return getDataSource(wallet.type);
    }

    private Single<WalletDataSourceType> getDataSource(String address) {
        return Single.fromCallable(() -> {
            for (WalletDataSourceType dataSource : dataSources) {
                if (dataSource.hasAddress(address)) {
                    return dataSource;
                }
            }
            return null;
        });
    }

    private Single<WalletDataSourceType> getDataSource(int type) {
        return Single.fromCallable(() -> {
            if (type == Wallet.MNEMONIC || type == Wallet.KEY_STORE) {
                return dataSources[0];
            } else {
                return dataSources[1];
            }
        });
    }
}