package com.wallet.crypto.mybitapp.ui.fragment.wallet;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.FetchTokensInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.ReceiveRouter;
import com.wallet.crypto.mybitapp.router.SendTokenRouter;

public class WalletViewModelFactory implements ViewModelProvider.Factory {

    private final FetchTokensInteract fetchTokensInteract;
    private final SendTokenRouter sendTokenRouter;
    private final ReceiveRouter receiveRouter;
    private final GetSessionInteract getSessionInteract;
    private final SessionRepositoryType sessionRepository;

    public WalletViewModelFactory(FetchTokensInteract fetchTokensInteract,
                                  SendTokenRouter sendTokenRouter,
                                  ReceiveRouter receiveRouter,
                                  GetSessionInteract getSessionInteract,
                                  SessionRepositoryType sessionRepository) {
        this.fetchTokensInteract = fetchTokensInteract;
        this.sendTokenRouter = sendTokenRouter;
        this.receiveRouter = receiveRouter;
        this.getSessionInteract = getSessionInteract;
        this.sessionRepository = sessionRepository;
    }

    @NonNull
    @Override
    public WalletViewModel create(@NonNull Class modelClass) {
        return new WalletViewModelImpl(
                fetchTokensInteract,
                sendTokenRouter,
                receiveRouter,
                getSessionInteract,
                sessionRepository);
    }
}
