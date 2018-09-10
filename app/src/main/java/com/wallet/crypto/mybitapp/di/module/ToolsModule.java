package com.wallet.crypto.mybitapp.di.module;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.wallet.crypto.mybitapp.App;
import com.wallet.crypto.mybitapp.repository.PasswordStore;
import com.wallet.crypto.mybitapp.repository.TrustPasswordStore;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;
import com.wallet.crypto.mybitapp.util.LogInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ToolsModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .build();
    }

    @Singleton
    @Provides
    PasswordStore passwordStore(Context context) {
        return new TrustPasswordStore(context);
    }

    @Singleton
    @Provides
    NetworkChangeReceiver networkChangeReceiver(Context context) {
        NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
        context.registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return networkChangeReceiver;
    }
}
