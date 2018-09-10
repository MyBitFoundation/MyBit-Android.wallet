package com.wallet.crypto.mybitapp.di.module;

import com.wallet.crypto.mybitapp.interact.FetchTokensInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.TokenRepositoryType;
import com.wallet.crypto.mybitapp.repository.WalletRepositoryType;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ReceiveRouter;
import com.wallet.crypto.mybitapp.router.SendTokenRouter;
import com.wallet.crypto.mybitapp.ui.fragment.wallet.WalletViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class TokensFragmentModule {

    @Provides
    WalletViewModelFactory provideTokensViewModelFactory(
            FetchTokensInteract fetchTokensInteract,
            SendTokenRouter sendTokenRouter,
            ReceiveRouter receiveRouter,
            GetSessionInteract getSessionInteract,
            SessionRepositoryType sessionRepository) {

        return new WalletViewModelFactory(
                fetchTokensInteract,
                sendTokenRouter,
                receiveRouter,
                getSessionInteract,
                sessionRepository);
    }

    @Provides
    FetchTokensInteract provideFetchTokensInteract(TokenRepositoryType tokenRepository) {
        return new FetchTokensInteract(tokenRepository);
    }

    @Provides
    SendTokenRouter provideSendTokenRouter() {
        return new SendTokenRouter();
    }

    @Provides
    ReceiveRouter provideMyAddressRouter() {
        return new ReceiveRouter();
    }

    @Provides
    GetSessionInteract provideGetSessionInteract(SessionRepositoryType sessionRepository, WalletRepositoryType walletRepository) {
        return new GetSessionInteract(sessionRepository, walletRepository);
    }
}
