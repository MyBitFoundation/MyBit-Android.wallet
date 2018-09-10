package com.wallet.crypto.mybitapp.ui.activity.wallets;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.CreateWalletInteract;
import com.wallet.crypto.mybitapp.interact.DefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.DeleteWalletInteract;
import com.wallet.crypto.mybitapp.interact.ExportWalletInteract;
import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.FindDefaultWalletInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;
import com.wallet.crypto.mybitapp.router.ImportWalletRouter;
import com.wallet.crypto.mybitapp.router.MainRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import javax.inject.Inject;

public class WalletsViewModelFactory implements ViewModelProvider.Factory {

    private final CreateWalletInteract createWalletInteract;
    private final DefaultWalletInteract defaultWalletInteract;
    private final DeleteWalletInteract deleteWalletInteract;
    private final FetchWalletsInteract fetchWalletsInteract;
    private final FindDefaultWalletInteract findDefaultWalletInteract;
    private final ExportWalletInteract exportWalletInteract;
    private final PassCodeInteract passCodeInteract;
    private final ImportWalletRouter importWalletRouter;
    private final MainRouter mainRouter;
    private final NetworkChangeReceiver networkChangeReceiver;

    @Inject
    public WalletsViewModelFactory(
            CreateWalletInteract createWalletInteract,
            DefaultWalletInteract defaultWalletInteract,
            DeleteWalletInteract deleteWalletInteract,
            FetchWalletsInteract fetchWalletsInteract,
            FindDefaultWalletInteract findDefaultWalletInteract,
            ExportWalletInteract exportWalletInteract,
            ImportWalletRouter importWalletRouter,
            MainRouter mainRouter,
            PassCodeInteract passCodeInteract,
            NetworkChangeReceiver networkChangeReceiver) {
        this.createWalletInteract = createWalletInteract;
        this.defaultWalletInteract = defaultWalletInteract;
        this.deleteWalletInteract = deleteWalletInteract;
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.exportWalletInteract = exportWalletInteract;
        this.importWalletRouter = importWalletRouter;
        this.mainRouter = mainRouter;
        this.passCodeInteract = passCodeInteract;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public WalletsViewModel create(@NonNull Class modelClass) {
        return new WalletsViewModelImpl(
                createWalletInteract,
                defaultWalletInteract,
                deleteWalletInteract,
                fetchWalletsInteract,
                findDefaultWalletInteract,
                exportWalletInteract,
                importWalletRouter,
                mainRouter,
                passCodeInteract,
                networkChangeReceiver);
    }
}
