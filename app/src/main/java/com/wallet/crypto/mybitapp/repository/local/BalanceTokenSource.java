package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 14.06.2018
 */
public interface BalanceTokenSource {

    Completable put(NetworkInfo networkInfo, Wallet wallet, Balance balance, String tokenAddress);

    Single<Map<String, Balance>> fetch(NetworkInfo networkInfo, Wallet wallet);

    Completable clear(NetworkInfo networkInfo, Wallet wallet);

}
