package com.wallet.crypto.mybitapp.ui.fragment.settings;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.repository.session.SessionRepositoryType;
import com.wallet.crypto.mybitapp.router.EmailRouter;
import com.wallet.crypto.mybitapp.router.ManageWalletsRouter;
import com.wallet.crypto.mybitapp.router.RateUsRouter;
import com.wallet.crypto.mybitapp.router.SocialRouter;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {

    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final PassCodeInteract passCodeInteract;
    private final ManageWalletsRouter manageWalletsRouter;
    private final SocialRouter socialRouter;
    private final EmailRouter emailRouter;
    private final RateUsRouter rateUsRouter;
    private final SessionRepositoryType sessionRepository;

    public SettingsViewModelFactory(FindDefaultWalletInteract findDefaultWalletInteract,
                                    PassCodeInteract passCodeInteract,
                                    ManageWalletsRouter manageWalletsRouter,
                                    SocialRouter socialRouter,
                                    EmailRouter emailRouter,
                                    RateUsRouter rateUsRouter,
                                    SessionRepositoryType sessionRepository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.passCodeInteract = passCodeInteract;
        this.manageWalletsRouter = manageWalletsRouter;
        this.socialRouter = socialRouter;
        this.emailRouter = emailRouter;
        this.rateUsRouter = rateUsRouter;
        this.sessionRepository = sessionRepository;
    }

    @NonNull
    @Override
    public SettingsViewModel create(@NonNull Class modelClass) {
        return new SettingsViewModelImpl(
                findDefaultWalletInteract,
                passCodeInteract,
                manageWalletsRouter,
                socialRouter,
                emailRouter,
                rateUsRouter,
                sessionRepository);
    }
}
