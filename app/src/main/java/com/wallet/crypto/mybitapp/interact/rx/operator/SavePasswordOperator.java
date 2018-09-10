package com.wallet.crypto.mybitapp.interact.rx.operator;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.observers.DisposableCompletableObserver;

public class SavePasswordOperator implements SingleTransformer<Wallet, Wallet> {

    private final PasswordStore passwordStore;
    private final String password;
    private final WalletRepositoryType walletRepository;

    public SavePasswordOperator(
            PasswordStore passwordStore, WalletRepositoryType walletRepository, String password) {
        this.passwordStore = passwordStore;
        this.password = password;
        this.walletRepository = walletRepository;
    }

    @Override
    public SingleSource<Wallet> apply(Single<Wallet> upstream) {
        return upstream.flatMap(wallet -> passwordStore
                .setPassword(wallet, password)
                .onErrorResumeNext(err -> walletRepository.deleteWallet(wallet, password)
                        .toCompletable()
                        .lift(observer -> new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                observer.onError(err);
                            }

                            @Override
                            public void onError(Throwable e) {
                                observer.onError(e);
                            }
                        }))
                .toSingle(() -> wallet));
    }
}
