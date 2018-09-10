package com.wallet.crypto.mybitapp.entity;

/**
 * Created by Dasha on 21.06.2018
 */
public class TransactionModel {

    public final Transaction[] transactions;
    public final boolean reset;

    public TransactionModel(Transaction[] transactions, boolean reset) {
        this.transactions = transactions;
        this.reset = reset;
    }
}
