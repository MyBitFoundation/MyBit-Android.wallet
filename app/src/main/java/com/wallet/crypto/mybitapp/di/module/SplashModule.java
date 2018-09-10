package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.ui.activity.splash.SplashViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {

    @Provides
    SplashViewModelFactory provideSplashViewModelFactory(FetchWalletsInteract fetchWalletsInteract, PassCodeInteract passCodeInteract) {
        return new SplashViewModelFactory(fetchWalletsInteract, passCodeInteract);
    }

    @Provides
    FetchWalletsInteract provideFetchWalletInteract(WalletRepositoryType walletRepository) {
        return new FetchWalletsInteract(walletRepository);
    }
}
