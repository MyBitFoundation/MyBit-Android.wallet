package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.di.scope.FragmentScope;
import com.wallet.crypto.mybitapp.ui.fragment.wallet.WalletFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface TokensModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = {TokensFragmentModule.class})
    WalletFragment tokensFragment();
}
