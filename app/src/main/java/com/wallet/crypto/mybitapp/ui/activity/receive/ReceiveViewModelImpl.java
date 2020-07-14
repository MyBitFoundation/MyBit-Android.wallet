package com.wallet.crypto.mybitapp.ui.activity.receive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class ReceiveViewModelImpl extends ReceiveViewModel {
    private final MutableLiveData<Bitmap> qrCodePictureCreated = new MutableLiveData<>();

    private final SessionRepositoryType sessionRepository;

    ReceiveViewModelImpl(SessionRepositoryType sessionRepository, NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        return sessionRepository.getNetwork();
    }

    @Override
    public LiveData<Bitmap> qrCodePictureCreated() {
        return qrCodePictureCreated;
    }
}
