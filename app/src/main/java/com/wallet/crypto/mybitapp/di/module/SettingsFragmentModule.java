package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.EmailRouter;
import com.wallet.crypto.mybitapp.router.ManageWalletsRouter;
import com.wallet.crypto.mybitapp.router.RateUsRouter;
import com.wallet.crypto.mybitapp.router.SocialRouter;
import com.wallet.crypto.mybitapp.ui.fragment.settings.SettingsViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsFragmentModule {
    @Provides
    SettingsViewModelFactory provideViewModelFactory(FindDefaultWalletInteract findDefaultWalletInteract,
                                                     PassCodeInteract passCodeInteract,
                                                     ManageWalletsRouter manageWalletsRouter,
                                                     SocialRouter socialRouter,
                                                     EmailRouter emailRouter,
                                                     RateUsRouter rateUsRouter,
                                                     SessionRepositoryType sessionRepository) {
        return new SettingsViewModelFactory(
                findDefaultWalletInteract,
                passCodeInteract,
                manageWalletsRouter,
                socialRouter,
                emailRouter,
                rateUsRouter,
                sessionRepository);
    }

    @Provides
    FindDefaultWalletInteract provideFindDefaultWalletInteract(WalletRepositoryType walletRepository) {
        return new FindDefaultWalletInteract(walletRepository);
    }

    @Provides
    ManageWalletsRouter provideManageWalletsRouter() {
        return new ManageWalletsRouter();
    }

    @Provides
    SocialRouter provideSocialRouter() {
        return new SocialRouter();
    }

    @Provides
    EmailRouter provideEmailRouter() {
        return new EmailRouter();
    }

    @Provides
    RateUsRouter provideRateUsRouter() {
        return new RateUsRouter();
    }
}
