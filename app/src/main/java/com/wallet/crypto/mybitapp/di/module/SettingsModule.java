package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.di.scope.FragmentScope;
import com.wallet.crypto.mybitapp.ui.fragment.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface SettingsModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = { SettingsFragmentModule.class })
    SettingsFragment settingsFragment();
}
