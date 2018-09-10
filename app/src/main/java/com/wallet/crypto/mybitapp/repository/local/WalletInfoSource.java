package com.wallet.crypto.mybitapp.repository.local;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.repository.entity.RealmWalletInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Dasha on 14.06.2018
 */
public interface WalletInfoSource {

    Completable put(Wallet wallet);

    Single<Wallet[]> fetch();

    Single<Boolean> hasAddress(String address);

    Completable delete(Wallet wallet);
}
