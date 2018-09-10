package com.wallet.crypto.mybitapp.entity;

/**
 * Created by Dasha on 20.06.2018
 */
public class Session {

    public final NetworkInfo networkInfo;
    public final Wallet wallet;

    public Session(NetworkInfo networkInfo, Wallet wallet) {
        this.networkInfo = networkInfo;
        this.wallet = wallet;
    }

}
