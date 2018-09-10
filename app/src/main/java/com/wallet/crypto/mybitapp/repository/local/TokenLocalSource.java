package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface TokenLocalSource {

    Completable put(NetworkInfo networkInfo, Wallet wallet, TokenInfo tokenInfo);

    Single<TokenInfo[]> fetch(NetworkInfo networkInfo, Wallet wallet);

    Completable clear(NetworkInfo networkInfo, Wallet wallet);
}
