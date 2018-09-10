package com.wallet.crypto.mybitapp.interact;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dasha on 20.06.2018
 */
public class GetSessionInteract {

    private final SessionRepositoryType sessionRepository;
    private final WalletRepositoryType walletRepository;

    public GetSessionInteract(SessionRepositoryType sessionRepositoryType, WalletRepositoryType walletRepository) {
        this.sessionRepository = sessionRepositoryType;
        this.walletRepository = walletRepository;
    }

    public Single<Session> get() {
        return sessionRepository.getSession()
                .onErrorResumeNext(error -> findDefaultWallet()
                        .flatMapCompletable(sessionRepository::setWallet)
                        .andThen(sessionRepository.getSession())
                )
                .subscribeOn(Schedulers.io());
    }

    private Single<Wallet> findDefaultWallet() {
        return walletRepository.getDefaultWallet()
                .onErrorResumeNext(walletRepository
                        .fetchWallets()
                        .to(single -> Flowable.fromArray(single.blockingGet()))
                        .firstOrError()
                        .flatMapCompletable(walletRepository::setDefaultWallet)
                        .andThen(walletRepository.getDefaultWallet()));
    }

}
