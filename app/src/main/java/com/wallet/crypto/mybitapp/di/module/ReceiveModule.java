package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.ui.activity.receive.ReceiveViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class ReceiveModule {
    @Provides
    ReceiveViewModelFactory provideReceiveViewModelFactory(SessionRepositoryType sessionRepository, NetworkChangeReceiver networkChangeReceiver) {
        return new ReceiveViewModelFactory(sessionRepository, networkChangeReceiver);
    }
}
