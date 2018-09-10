package com.wallet.crypto.mybitapp.ui.widget;

import android.view.View;

import com.wallet.crypto.mybitapp.entity.Transaction;

public interface OnTransactionClickListener {
    void onTransactionClick(View view, Transaction transaction);
}
