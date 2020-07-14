package com.wallet.crypto.mybitapp.ui.activity.receive;

import androidx.lifecycle.LiveData;
import android.graphics.Bitmap;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public abstract class ReceiveViewModel extends NoInternetConnectionBaseViewModel {
    public ReceiveViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract NetworkInfo getNetworkInfo();

    public abstract LiveData<Bitmap> qrCodePictureCreated();
}
