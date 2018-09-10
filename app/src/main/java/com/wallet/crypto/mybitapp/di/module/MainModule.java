package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.ui.activity.main.MainViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    @Provides
    MainViewModelFactory provideMainViewModelFactory(NetworkChangeReceiver networkChangeReceiver) {
        return new MainViewModelFactory(networkChangeReceiver);
    }
}
