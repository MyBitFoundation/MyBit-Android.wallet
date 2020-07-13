package com.wallet.crypto.mybitapp.ui.activity.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.interact.FetchWalletsInteract;
import com.wallet.crypto.mybitapp.interact.PassCodeInteract;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashViewModelImpl extends SplashViewModel {
    private final FetchWalletsInteract fetchWalletsInteract;
    private final PassCodeInteract passCodeInteract;

    private MutableLiveData<Wallet[]> wallets = new MutableLiveData<>();
    private MutableLiveData<Boolean> onOpenPin = new MutableLiveData<>();

    SplashViewModelImpl(FetchWalletsInteract fetchWalletsInteract, PassCodeInteract passCodeInteract) {
        this.fetchWalletsInteract = fetchWalletsInteract;
        this.passCodeInteract = passCodeInteract;
    }

    @Override
    public void prepare() {
        compositeDisposable.add(passCodeInteract
                .checkIsOptioned()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    if (e) {
                        onOpenPin.postValue(e);
                    } else {
                        fetchWallets();
                    }
                }, Throwable::printStackTrace));

        compositeDisposable.add(passCodeInteract
                .pinCodeEnteredSuccessful()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> fetchWallets()));
    }

    private void fetchWallets() {
        compositeDisposable.add(fetchWalletsInteract
                .fetch()
                .subscribe(wallets::postValue, this::onError));
    }

    @Override
    protected void onError(Throwable throwable) {
        wallets.postValue(new Wallet[0]);
    }

    @Override
    public LiveData<Wallet[]> wallets() {
        return wallets;
    }

    @Override
    public LiveData<Boolean> onOpenPin() {
        return onOpenPin;
    }
}
