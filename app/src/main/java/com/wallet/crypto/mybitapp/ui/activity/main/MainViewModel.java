package com.wallet.crypto.mybitapp.ui.activity.main;

import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public abstract class MainViewModel extends NoInternetConnectionBaseViewModel {
    public MainViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }
}
