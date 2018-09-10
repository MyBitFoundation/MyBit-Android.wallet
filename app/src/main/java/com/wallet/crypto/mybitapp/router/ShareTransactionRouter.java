package com.wallet.crypto.mybitapp.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.util.UriUtils;

/**
 * Created by Dasha on 22.06.2018
 */
public class ShareTransactionRouter {

    public void open(Context context, Transaction transaction, NetworkInfo networkInfo) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject_transaction_detail));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, UriUtils.buildEtherscanUri(transaction, networkInfo).toString());
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void open(Context context, Uri etherscanUri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject_transaction_detail));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, etherscanUri.toString());
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
