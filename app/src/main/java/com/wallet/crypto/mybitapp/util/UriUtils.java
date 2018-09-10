package com.wallet.crypto.mybitapp.util;

import android.net.Uri;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Transaction;

/**
 * Created by Dasha on 22.06.2018
 */
public class UriUtils {

    public static Uri buildEtherscanUri(Transaction transaction, NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.etherscanUrl != null && !networkInfo.etherscanUrl.isEmpty()) {
            return Uri.parse(networkInfo.etherscanUrl)
                    .buildUpon()
                    .appendEncodedPath("tx")
                    .appendEncodedPath(transaction.hash)
                    .build();
        }

        return null;
    }
}
