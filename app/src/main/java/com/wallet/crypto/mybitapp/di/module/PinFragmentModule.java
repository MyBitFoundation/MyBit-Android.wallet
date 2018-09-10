package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.ui.fragment.pin.PinViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class PinFragmentModule {
    @Provides
    PinViewModelFactory providePinViewModelFactory(PassCodeInteract passCodeInteract) {
        return new PinViewModelFactory(passCodeInteract);
    }
}
