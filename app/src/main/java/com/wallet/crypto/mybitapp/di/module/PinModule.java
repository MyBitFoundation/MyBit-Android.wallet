package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.di.scope.FragmentScope;
import com.wallet.crypto.mybitapp.ui.fragment.pin.PinFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface PinModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = { PinFragmentModule.class })
    PinFragment pinFragment();
}
