package com.wallet.crypto.mybitapp.ui.fragment.pin;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.PassCodeInteract;

import javax.inject.Inject;

public class PinViewModelFactory implements ViewModelProvider.Factory {
    private final PassCodeInteract passCodeInteract;

    @Inject
    public PinViewModelFactory(PassCodeInteract passCodeInteract) {
        this.passCodeInteract = passCodeInteract;
    }

    @NonNull
    @Override
    public PinViewModel create(@NonNull Class modelClass) {
        return new PinViewModelImpl(passCodeInteract);
    }
}
