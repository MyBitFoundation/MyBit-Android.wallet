package com.wallet.crypto.mybitapp.entity;


public class GasModel {

    public final String gasPrice;
    public final String gasLimit;
    public final String networkFee;

    public GasModel(String gasPrice, String gasLimit, String networkFee) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.networkFee = networkFee;
    }
}
