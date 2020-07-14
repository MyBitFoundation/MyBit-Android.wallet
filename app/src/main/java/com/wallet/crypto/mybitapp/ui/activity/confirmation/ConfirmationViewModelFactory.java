package com.wallet.crypto.mybitapp.ui.activity.confirmation;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.CreateTransactionInteract;
import com.wallet.crypto.mybitapp.interact.FetchGasSettingsInteract;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.router.GasSettingsRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class ConfirmationViewModelFactory implements ViewModelProvider.Factory {

    private final GetSessionInteract getSessionInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;
    private final GasSettingsRouter gasSettingsRouter;
    private final NetworkChangeReceiver networkChangeReceiver;

    public ConfirmationViewModelFactory(GetSessionInteract getSessionInteract,
                                        FetchGasSettingsInteract fetchGasSettingsInteract,
                                        CreateTransactionInteract createTransactionInteract,
                                        GasSettingsRouter gasSettingsRouter,
                                        NetworkChangeReceiver networkChangeReceiver) {
        this.getSessionInteract = getSessionInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
        this.gasSettingsRouter = gasSettingsRouter;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public ConfirmationViewModel create(@NonNull Class modelClass) {
        return new ConfirmationViewModelImpl(getSessionInteract, fetchGasSettingsInteract, createTransactionInteract, gasSettingsRouter, networkChangeReceiver);
    }
}
