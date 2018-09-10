package com.wallet.crypto.mybitapp.repository.session;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 20.06.2018
 */
public interface SessionRepositoryType {

    void addOnSessionChangeListener(OnSessionChangeListener onSessionChangeListener);

    Single<Session> getSession();

    Completable setWallet(Wallet wallet);

    Completable clearDefaultWallet();

    NetworkInfo getNetwork();

}
