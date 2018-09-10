package com.wallet.crypto.mybitapp.repository.wallet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.WalletAlreadyExistsException;

import org.ethereum.geth.KeyStore;
import org.web3j.crypto.WalletFile;
import org.ethereum.geth.Account;

import java.nio.charset.Charset;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddKeyStoreDataSource implements AddDataSourceType {
    @Override
    public Single<Wallet> add(String value, String password, String newPassword, Func1<String, Boolean> hasAddress, Action1<Wallet> putWalletInfo, KeyStore keyStore) {
        return Single
                .fromCallable(() -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    WalletFile walletFile = objectMapper.readValue(value, WalletFile.class);
                    if (!hasAddress.call(walletFile.getAddress())) {
                        return value;
                    }
                    throw new WalletAlreadyExistsException();
                })
                .map(store -> {
                    Account account = keyStore
                            .importKey(store.getBytes(Charset.forName("UTF-8")), password, newPassword);
                    return new Wallet(account.getAddress().getHex().toLowerCase(), getType());
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public int getType() {
        return Wallet.KEY_STORE;
    }
}
