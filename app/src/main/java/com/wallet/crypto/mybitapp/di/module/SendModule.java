package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.router.ConfirmationRouter;
import com.wallet.crypto.mybitapp.ui.activity.send.SendViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class SendModule {
    @Provides
    SendViewModelFactory provideSendViewModelFactory(ConfirmationRouter confirmationRouter, NetworkChangeReceiver networkChangeReceiver) {
        return new SendViewModelFactory(confirmationRouter, networkChangeReceiver);
    }

    @Provides
    ConfirmationRouter provideConfirmationRouter() {
        return new ConfirmationRouter();
    }
}
