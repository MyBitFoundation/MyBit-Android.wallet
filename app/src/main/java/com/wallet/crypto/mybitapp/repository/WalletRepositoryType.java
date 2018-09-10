package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface WalletRepositoryType {
    Single<Wallet[]> fetchWallets();

    Single<Wallet> findWallet(String address);

    Single<Wallet> createWallet(String password);

    Single<Wallet> addWallet(int type, String value, String password, String newPassword);

    Single<String> exportWallet(Wallet wallet, String password, String newPassword);

    Single<Wallet[]> deleteWallet(Wallet wallet, String password);

    Single<Wallet> removeWatchedDuplicateWallet(Wallet wallet);

    Completable setDefaultWallet(Wallet wallet);

    Single<Wallet> getDefaultWallet();

}
