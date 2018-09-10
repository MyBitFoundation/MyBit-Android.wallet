package com.wallet.crypto.mybitapp;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.wallet.crypto.mybitapp.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.DaggerApplication;
import io.realm.Realm;

public class App extends DaggerApplication implements HasActivityInjector {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            multiDexException.printStackTrace();
        }
    }
}