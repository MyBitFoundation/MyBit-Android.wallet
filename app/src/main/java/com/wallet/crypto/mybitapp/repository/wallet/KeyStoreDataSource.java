package com.wallet.crypto.mybitapp.repository.wallet;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.ServiceException;

import org.ethereum.geth.Accounts;
import org.ethereum.geth.Address;
import org.ethereum.geth.BigInt;
import org.ethereum.geth.Geth;
import org.ethereum.geth.KeyStore;
import org.ethereum.geth.Transaction;

import java.io.File;
import java.math.BigInteger;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class KeyStoreDataSource extends BaseKeyStoreDataSource {
    private final KeyStore keyStore;

    public KeyStoreDataSource(File keyStoreFile) {
        keyStore = new KeyStore(keyStoreFile.getAbsolutePath(), Geth.LightScryptN, Geth.LightScryptP);
    }

    @Override
    public Single<Wallet> create(String password) {
        return Single.fromCallable(() -> new Wallet(
                keyStore.newAccount(password).getAddress().getHex().toLowerCase()))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Wallet> add(String value, String password, String newPassword, int type) {
        createDataSourceType(type);
        return addDataSourceTypeStrategy.add(value, password, newPassword, this::hasAddress, null, keyStore);
    }

    @Override
    public Single<String> export(Wallet wallet, String password, String newPassword) {
        return Single
                .fromCallable(() -> findAccount(wallet.address))
                .flatMap(account1 -> Single.fromCallable(()
                        -> new String(keyStore.exportKey(account1, password, newPassword))))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable delete(Wallet wallet, String password) {
        return Single.fromCallable(() -> findAccount(wallet.address))
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(account -> Completable.fromAction(
                        () -> keyStore.deleteAccount(account, password))
                        .observeOn(Schedulers.io()));
    }

    @Override
    public Single<Wallet[]> fetchAll() {
        return Single.fromCallable(() -> {
            Accounts accounts = keyStore.getAccounts();
            int len = (int) accounts.size();
            Wallet[] result = new Wallet[len];

            for (int i = 0; i < len; i++) {
                org.ethereum.geth.Account gethAccount = accounts.get(i);
                result[i] = new Wallet(gethAccount.getAddress().getHex().toLowerCase());
            }
            return result;
        });
    }

    @Override
    public Single<byte[]> signTransaction(Wallet signer, String signerPassword, String toAddress, BigInteger amount, BigInteger gasPrice,
                                          BigInteger gasLimit, long nonce, byte[] data, long chainId) {
        return Single.fromCallable(() -> {
            BigInt value = new BigInt(0);
            value.setString(amount.toString(), 10);

            BigInt gasPriceBI = new BigInt(0);
            gasPriceBI.setString(gasPrice.toString(), 10);

            BigInt gasLimitBI = new BigInt(0);
            gasLimitBI.setString(gasLimit.toString(), 10);

            Transaction tx = new Transaction(
                    nonce,
                    new Address(toAddress),
                    value,
                    gasLimitBI,
                    gasPriceBI,
                    data);

            BigInt chain = new BigInt(chainId); // Chain identifier of the main net
            org.ethereum.geth.Account gethAccount = findAccount(signer.address);
            keyStore.unlock(gethAccount, signerPassword);
            Transaction signed = keyStore.signTx(gethAccount, tx, chain);
            keyStore.lock(gethAccount.getAddress());

            return signed.encodeRLP();

        }).subscribeOn(Schedulers.io());
    }

    @Override
    public boolean hasAddress(String address) {
        return keyStore.hasAddress(new Address(address));
    }

    private org.ethereum.geth.Account findAccount(String address) throws ServiceException {
        Accounts accounts = keyStore.getAccounts();

        int len = (int) accounts.size();
        for (int i = 0; i < len; i++) {
            try {
                android.util.Log.d("ACCOUNT_FIND", "Address: " + accounts.get(i).getAddress().getHex());
                if (accounts.get(i).getAddress().getHex().equalsIgnoreCase(address)) {
                    return accounts.get(i);
                }
            } catch (Exception ex) {
                /* Quietly: interest only result, maybe next is ok. */
            }
        }
        throw new ServiceException("Wallet with address: " + address + " not found");
    }
}
