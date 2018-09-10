package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress);

    Completable clear(Wallet wallet);
}
