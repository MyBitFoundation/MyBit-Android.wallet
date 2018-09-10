package com.wallet.crypto.mybitapp.router;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class RateUsRouter {
    private static final String MARKET_DETAILS = "market://details?id=";
    private static final String MARKET_URI = "http://play.google.com/store/apps/details?id=";

    public void open(Context context) {
        Uri uri = Uri.parse(MARKET_DETAILS + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        tryOpenIntent(context, goToMarket);
    }

    private void tryOpenIntent(Context context, Intent goToMarketIntent) {
        try {
            context.startActivity(goToMarketIntent);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI + context.getPackageName())));
        }
    }
}
