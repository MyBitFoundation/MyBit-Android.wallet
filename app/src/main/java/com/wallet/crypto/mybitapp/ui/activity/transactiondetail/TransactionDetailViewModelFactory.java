package com.wallet.crypto.mybitapp.ui.activity.transactiondetail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.router.ExternalBrowserRouter;
import com.wallet.crypto.mybitapp.router.ShareTransactionRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public class TransactionDetailViewModelFactory implements ViewModelProvider.Factory {

    private final GetSessionInteract getSessionInteract;
    private final ExternalBrowserRouter externalBrowserRouter;
    private final ShareTransactionRouter shareTransactionRouter;
    private final NetworkChangeReceiver networkChangeReceiver;

    public TransactionDetailViewModelFactory(
            GetSessionInteract getSessionInteract,
            ExternalBrowserRouter externalBrowserRouter,
            ShareTransactionRouter shareTransactionRouter,
            NetworkChangeReceiver networkChangeReceiver) {
        this.getSessionInteract = getSessionInteract;
        this.externalBrowserRouter = externalBrowserRouter;
        this.shareTransactionRouter = shareTransactionRouter;
        this.networkChangeReceiver = networkChangeReceiver;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TransactionDetailViewModelImpl(
                getSessionInteract,
                externalBrowserRouter,
                shareTransactionRouter,
                networkChangeReceiver);
    }
}
