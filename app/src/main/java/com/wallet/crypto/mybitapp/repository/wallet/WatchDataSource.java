package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.local.WalletInfoSource;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 18.06.2018
 */
public class WatchDataSource extends BaseKeyStoreDataSource {

    private final WalletInfoSource walletInfoSource;

    public WatchDataSource(WalletInfoSource walletInfoSource) {
        this.walletInfoSource = walletInfoSource;
    }

    @Override
    public Single<Wallet> create(String password) {
        return null;
    }

    @Override
    public Single<Wallet> add(String value, String password, String newPassword, int type) {
        createDataSourceType(type);
        return addDataSourceTypeStrategy.add(value, password, newPassword, null, this::walletInfoPut, null);
    }

    @Override
    public Single<String> export(Wallet wallet, String password, String newPassword) {
        return null;
    }

    @Override
    public Completable delete(Wallet wallet, String password) {
        return walletInfoSource.delete(wallet);
    }

    @Override
    public Single<byte[]> signTransaction(Wallet signer, String signerPassword, String toAddress, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, long nonce, byte[] data, long chainId) {
        return null;
    }

    @Override
    public Single<Wallet[]> fetchAll() {
        return walletInfoSource.fetch();
    }

    @Override
    public boolean hasAddress(String address) {
        return false;
    }

    private void walletInfoPut(Wallet wallet) {
        walletInfoSource.put(wallet).blockingAwait();
    }
}
