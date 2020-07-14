package com.wallet.crypto.mybitapp.ui.activity.send;

import androidx.lifecycle.ViewModelProvider;

import com.wallet.crypto.mybitapp.router.ConfirmationRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import io.reactivex.annotations.NonNull;

public class SendViewModelFactory implements ViewModelProvider.Factory {

    private final ConfirmationRouter confirmationRouter;
    private final NetworkChangeReceiver networkChangeReceiver;

    public SendViewModelFactory(ConfirmationRouter confirmationRouter, NetworkChangeReceiver networkChangeReceiver) {
        this.confirmationRouter = confirmationRouter;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public SendViewModel create(@NonNull Class modelClass) {
        return new SendViewModelImpl(confirmationRouter, networkChangeReceiver);
    }
}
