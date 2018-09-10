package com.wallet.crypto.mybitapp.repository.entity;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dasha on 14.06.2018
 */
public class RealmBalanceToken extends RealmObject {

    @Ignore
    public static final String COL_TOKEN_ADDRESS = "tokenAddress";

    @PrimaryKey
    private String tokenAddress;
    private String cryptoBalance;
    private String usdPrice;
    private String percentChange24h;

    public RealmBalanceToken() {
    }

    public RealmBalanceToken(String tokenAddress, String cryptoBalance, String usdPrice, String percentChange24h) {
        this.tokenAddress = tokenAddress;
        this.cryptoBalance = cryptoBalance;
        this.usdPrice = usdPrice;
        this.percentChange24h = percentChange24h;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getCryptoBalance() {
        return cryptoBalance;
    }

    public void setCryptoBalance(String cryptoBalance) {
        this.cryptoBalance = cryptoBalance;
    }

    public String getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(String usdPrice) {
        this.usdPrice = usdPrice;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }
}
