package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.ui.activity.wallets.WalletsActivity;
import com.wallet.crypto.mybitapp.ui.base.BaseActivity;

public class ManageWalletsRouter {

    public void openForResult(Context context, boolean isClearStack) {
        Intent intent = new Intent(context, WalletsActivity.class);
        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ((BaseActivity) context).startActivityForResult(intent, C.WALLET_REQUEST_CODE);
    }

    public void open(AppCompatActivity activity) {
        Intent intent = new Intent(activity, WalletsActivity.class);
        activity.startActivity(intent);
    }
}
