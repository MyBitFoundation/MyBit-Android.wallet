package com.wallet.crypto.mybitapp.ui.activity.main;

import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class MainViewModelImpl extends MainViewModel {

    public MainViewModelImpl(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
