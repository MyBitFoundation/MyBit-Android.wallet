package com.wallet.crypto.mybitapp.ui.activity.gassettings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import java.math.BigInteger;

public abstract class GasSettingsViewModel extends NoInternetConnectionBaseViewModel {

    public GasSettingsViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract void prepare();

    public abstract MutableLiveData<BigInteger> gasPrice();

    public abstract MutableLiveData<BigInteger> gasLimit();

    public abstract LiveData<NetworkInfo> defaultNetwork();

    public abstract BigInteger networkFee();
}
