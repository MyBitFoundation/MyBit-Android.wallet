package com.wallet.crypto.mybitapp.ui.activity.main;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final NetworkChangeReceiver networkChangeReceiver;

    public MainViewModelFactory(NetworkChangeReceiver networkChangeReceiver) {
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public MainViewModel create(@NonNull Class modelClass) {
        return new MainViewModelImpl(networkChangeReceiver);
    }
}