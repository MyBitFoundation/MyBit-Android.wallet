package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.util.UriUtils;

public class ExternalBrowserRouter {

    public void open(Context context, Uri uri) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(launchBrowser);
    }

    public void open(Context context, Transaction transaction, NetworkInfo networkInfo) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, UriUtils.buildEtherscanUri(transaction, networkInfo));
        context.startActivity(launchBrowser);
    }

}
