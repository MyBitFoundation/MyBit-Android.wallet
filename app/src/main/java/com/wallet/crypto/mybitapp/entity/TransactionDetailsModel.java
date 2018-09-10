package com.wallet.crypto.mybitapp.entity;

/**
 * Created by Dasha on 22.06.2018
 */
public class TransactionDetailsModel {

    public final String from;
    public final String to;
    public final String gasFee;
    public final String txHash;
    public final String txTime;
    public final String blockNumber;
    public final boolean isSent;
    public final String value;

    public TransactionDetailsModel(String from, String to, String gasFee, String txHash, String txTime, String blockNumber, boolean isSent, String value) {
        this.from = from;
        this.to = to;
        this.gasFee = gasFee;
        this.txHash = txHash;
        this.txTime = txTime;
        this.blockNumber = blockNumber;
        this.isSent = isSent;
        this.value = value;
    }
}
