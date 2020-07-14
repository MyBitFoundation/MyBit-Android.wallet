package com.wallet.crypto.mybitapp.ui.activity.receive;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import javax.inject.Inject;

public class ReceiveViewModelFactory implements ViewModelProvider.Factory {
    private final SessionRepositoryType sessionRepository;
    private final NetworkChangeReceiver networkChangeReceiver;

    @Inject
    public ReceiveViewModelFactory(SessionRepositoryType sessionRepository, NetworkChangeReceiver networkChangeReceiver) {
        this.sessionRepository = sessionRepository;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public ReceiveViewModel create(@NonNull Class modelClass) {
        return new ReceiveViewModelImpl(sessionRepository, networkChangeReceiver);
    }
}
