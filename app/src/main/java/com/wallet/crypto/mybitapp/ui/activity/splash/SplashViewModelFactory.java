package com.wallet.crypto.mybitapp.ui.activity.splash;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;

public class SplashViewModelFactory implements ViewModelProvider.Factory {

    private final FetchWalletsInteract fetchWalletsInteract;
    private final PassCodeInteract passCodeInteract;

    public SplashViewModelFactory(FetchWalletsInteract fetchWalletsInteract, PassCodeInteract passCodeInteract) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.passCodeInteract = passCodeInteract;
    }

    @NonNull
    @Override
    public SplashViewModel create(@NonNull Class modelClass) {
        return new SplashViewModelImpl(fetchWalletsInteract, passCodeInteract);
    }
}
