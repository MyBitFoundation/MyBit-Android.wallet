package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.ui.activity.send.SendActivity;

public class SendRouter {

    public void open(Context context) {
        Intent intent = new Intent(context, SendActivity.class);
        context.startActivity(intent);
    }
}
