package com.wallet.crypto.mybitapp.interact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.WalletAlreadyExistsException;
import com.wallet.crypto.mybitapp.interact.rx.operator.Operators;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;

import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletInteract {

    private final WalletRepositoryType walletRepository;
    private final PasswordStore passwordStore;

    public ImportWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet> importKeystore(String store, String password) {
        return passwordStore.generatePassword()
                .subscribeOn(Schedulers.io())
                .flatMap(newPassword -> walletRepository
                        .addWallet(Wallet.KEY_STORE, store, password, newPassword)
                        .compose(Operators.savePassword(passwordStore, walletRepository, newPassword))
                )
                .flatMap(walletRepository::removeWatchedDuplicateWallet);
        //.observeOn(AndroidSchedulers.mainThread()));
    }

    public Single<Wallet> importPrivateKey(String privateKey) {
        return passwordStore.generatePassword()
                .subscribeOn(Schedulers.io())
                .flatMap(newPassword -> convertPrivateKeyToStore(privateKey, newPassword)
                        .flatMap(store -> walletRepository.addWallet(Wallet.KEY_STORE, store, newPassword, newPassword))
                        .compose(Operators.savePassword(passwordStore, walletRepository, newPassword))
                )
                .flatMap(walletRepository::removeWatchedDuplicateWallet);
        //.observeOn(AndroidSchedulers.mainThread()));
    }

    private Single<String> convertPrivateKeyToStore(String key, String password) {
        return Single.fromCallable(() ->
                new ObjectMapper().writeValueAsString(
                        org.web3j.crypto.Wallet.create(
                                password,
                                ECKeyPair.create(new BigInteger(key, 16)),
                                512,
                                1))
        );
    }

    public Single<Wallet> importMnemonic(String mnemonic) {
        return passwordStore.generatePassword()
                .subscribeOn(Schedulers.io())
                .flatMap(newPassword -> walletRepository
                        .addWallet(Wallet.MNEMONIC, mnemonic, "", newPassword)
                        .compose(Operators.savePassword(passwordStore, walletRepository, newPassword))
                )
                .flatMap(walletRepository::removeWatchedDuplicateWallet);
        //.observeOn(AndroidSchedulers.mainThread()));
    }

    public Single<Wallet> importWatchAddress(String address) {
        return Single
                .create((SingleOnSubscribe<Wallet>) emitter -> {

                    Wallet wallet = findWallet(address);
                    if (wallet == null) {
                        wallet = walletRepository.addWallet(Wallet.WATCH, address, null, null).blockingGet();
                        emitter.onSuccess(wallet);

                    } else {
                        emitter.onError(new WalletAlreadyExistsException());
                    }

                })
                .subscribeOn(Schedulers.io());
    }

    private Wallet findWallet(String address) {
        try {
            return walletRepository.findWallet(address).blockingGet();
        } catch (NullPointerException ex) {
            return null;
        }
    }
}
