package com.wallet.crypto.mybitapp.ui.widget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.rxrelay2.BehaviorRelay;

import io.reactivex.Observable;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private final BehaviorRelay<Boolean> networkConnectionChanged = BehaviorRelay.create();

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getExtras() != null) {
                acceptIsOnline(context);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void requestForChangingNetworkConnection(Context context) {
        acceptIsOnline(context);
    }

    private void acceptIsOnline(Context context) {
        networkConnectionChanged.accept(isOnline(context));
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Observable<Boolean> onNetworkConnectionChanged() {
        return networkConnectionChanged;
    }
}
