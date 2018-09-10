package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.di.scope.FragmentScope;
import com.wallet.crypto.mybitapp.ui.fragment.transactions.TransactionsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface TransactionsModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = { TransactionsFragmentModule.class })
    TransactionsFragment transactionsFragment();
}