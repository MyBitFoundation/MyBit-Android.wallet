package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.ui.activity.transactiondetail.TransactionDetailActivity;

import static com.wallet.crypto.mybitapp.C.Key.TRANSACTION;

public class TransactionDetailRouter {

    public void open(Context context, Transaction transaction) {
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TRANSACTION, transaction);
        context.startActivity(intent);
    }
}
