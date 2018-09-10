package com.wallet.crypto.mybitapp.ui.activity.gassettings;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class GasSettingsViewModelFactory implements ViewModelProvider.Factory {

    private final GetSessionInteract getSessionInteract;
    private final NetworkChangeReceiver networkChangeReceiver;

    public GasSettingsViewModelFactory(GetSessionInteract getSessionInteract, NetworkChangeReceiver networkChangeReceiver) {
        this.getSessionInteract = getSessionInteract;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GasSettingsViewModelImpl(getSessionInteract, networkChangeReceiver);
    }
}
