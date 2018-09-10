package com.wallet.crypto.mybitapp.ui.activity.send;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import java.math.BigDecimal;

public abstract class SendViewModel extends NoInternetConnectionBaseViewModel {
    public SendViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract void tryOpenConfirmation(Context context,
                                             String toAddress,
                                             String amount,
                                             int decimals,
                                             String contractAddress,
                                             String symbol,
                                             boolean sendingTokens,
                                             BigDecimal balance);

    public abstract LiveData<Object> setValidAddress();

    public abstract LiveData<Object> setValidAmount();

    public abstract LiveData<Object> setErrorInvalidAddress();

    public abstract LiveData<Object> setErrorInvalidAmount();

    public abstract LiveData<Object> setErrorAmountPrecisionEth();

    public abstract LiveData<Object> setErrorAmountPrecisionMybit();
}
