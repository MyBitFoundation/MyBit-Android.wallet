package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;

public class EmailRouter {
    private static final String EMAIL_ADDRESS = "ian@mybit.io";
    private static final String EMAIL_CLIENT_INTENT_TYPE = "plain/text";

    public void open(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(EMAIL_CLIENT_INTENT_TYPE);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { EMAIL_ADDRESS });
        context.startActivity(Intent.createChooser(intent, ""));
    }
}
