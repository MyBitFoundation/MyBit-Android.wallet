package com.wallet.crypto.mybitapp.di.module;


import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.ui.activity.gassettings.GasSettingsViewModelFactory;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class GasSettingsModule {

    @Provides
    public GasSettingsViewModelFactory provideGasSettingsViewModelFactory(GetSessionInteract getSessionInteract, NetworkChangeReceiver networkChangeReceiver) {
        return new GasSettingsViewModelFactory(getSessionInteract, networkChangeReceiver);
    }

    @Provides
    GetSessionInteract provideFindDefaultNetworkInteract(
            SessionRepositoryType sessionRepository, WalletRepositoryType walletRepository) {
        return new GetSessionInteract(sessionRepository, walletRepository);
    }
}
