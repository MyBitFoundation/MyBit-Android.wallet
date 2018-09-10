package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;

import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 15.06.2018
 */
public interface WalletDataSourceType {

    Single<Wallet> create(String password);

    Single<Wallet> add(String value, String password, String newPassword, int type);

    Single<String> export(Wallet wallet, String password, String newPassword);

    Completable delete(Wallet wallet, String password);

    Single<byte[]> signTransaction(
            Wallet signer,
            String signerPassword,
            String toAddress,
            BigInteger amount,
            BigInteger gasPrice,
            BigInteger gasLimit,
            long nonce,
            byte[] data,
            long chainId);

    Single<Wallet[]> fetchAll();

    boolean hasAddress(String address);
}
