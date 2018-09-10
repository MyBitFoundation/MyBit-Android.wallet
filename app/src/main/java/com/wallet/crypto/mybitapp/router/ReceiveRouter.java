package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.activity.receive.ReceiveActivity;

import static com.wallet.crypto.mybitapp.C.Key.WALLET;

public class ReceiveRouter {

    public void open(Context context, Wallet wallet) {
        Intent intent = new Intent(context, ReceiveActivity.class);
        intent.putExtra(WALLET, wallet);
        context.startActivity(intent);
    }
}
