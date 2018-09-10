package com.wallet.crypto.mybitapp.interact;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Delete and fetch wallets
 */
public class DeleteWalletInteract {
    private final WalletRepositoryType walletRepository;
    private final PasswordStore passwordStore;

    public DeleteWalletInteract(WalletRepositoryType walletRepository, PasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet[]> delete(Wallet wallet) {
            return passwordStore.getPassword(wallet)
                    .flatMap(password -> walletRepository.deleteWallet(wallet, password));
    }
}