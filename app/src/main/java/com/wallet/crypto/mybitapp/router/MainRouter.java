package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.ui.activity.main.MainActivity;

public class MainRouter {
    public void open(Context context, boolean isClearStack) {
        Intent intent = new Intent(context, MainActivity.class);
        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }
}
