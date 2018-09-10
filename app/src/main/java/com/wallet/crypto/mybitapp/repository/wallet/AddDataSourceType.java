package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;

import org.ethereum.geth.KeyStore;

import io.reactivex.Single;
import rx.functions.Action1;
import rx.functions.Func1;

public interface AddDataSourceType {

    Single<Wallet> add(String value, String password, String newPassword, Func1<String, Boolean> hasAddress, Action1<Wallet> putWalletInfo, KeyStore keyStore);

    int getType();
}
