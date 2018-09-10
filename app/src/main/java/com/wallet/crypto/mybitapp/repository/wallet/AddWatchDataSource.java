package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;

import org.ethereum.geth.KeyStore;

import io.reactivex.Single;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddWatchDataSource implements AddDataSourceType {
    @Override
    public Single<Wallet> add(String value, String password, String newPassword, Func1<String, Boolean> hasAddress, Action1<Wallet> putWalletInfo, KeyStore keyStore) {
        return Single.fromCallable(() -> {
            Wallet wallet = new Wallet(value, getType());
            putWalletInfo.call(wallet);
            return wallet;
        });
    }

    @Override
    public int getType() {
        return Wallet.WATCH;
    }
}
