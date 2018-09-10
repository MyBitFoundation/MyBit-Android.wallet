package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SocialRouter {
    public void open(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }
}
