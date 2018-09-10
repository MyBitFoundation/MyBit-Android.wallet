package com.wallet.crypto.mybitapp.ui.activity.confirmation;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.entity.GasModel;
import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class ConfirmationViewModel extends NoInternetConnectionBaseViewModel {

    public ConfirmationViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract void sendAmount(String fromAddress, String toAddress, String contractAddress,
                                    BigInteger amount, BigDecimal balance);

    public abstract LiveData<Session> session();

    public abstract MutableLiveData<GasModel> gasModel();

    public abstract MutableLiveData<GasSettings> gasSettings();

    public abstract LiveData<String> sendTransaction();

    public abstract void prepare(boolean confirmationForTokenTransfer);

    public abstract void openGasSettings(Activity context);
}
