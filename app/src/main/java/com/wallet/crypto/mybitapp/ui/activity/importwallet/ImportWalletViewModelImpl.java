package com.wallet.crypto.mybitapp.ui.activity.importwallet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.entity.exception.ServiceException;
import com.wallet.crypto.mybitapp.entity.exception.WalletAlreadyExistsException;
import com.wallet.crypto.mybitapp.entity.exception.WalletException;
import com.wallet.crypto.mybitapp.interact.ImportWalletInteract;
import com.wallet.crypto.mybitapp.interact.WalletValidationsInteract;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletViewModelImpl extends ImportWalletViewModel {

    private final ImportWalletInteract importWalletInteract;
    private final WalletValidationsInteract walletValidationsInteract;
    private final MutableLiveData<Wallet> wallet = new MutableLiveData<>();

    ImportWalletViewModelImpl(ImportWalletInteract importWalletInteract, WalletValidationsInteract walletValidationsInteract,
                              NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.importWalletInteract = importWalletInteract;
        this.walletValidationsInteract = walletValidationsInteract;
    }

    @Override
    public void onKeystore(String keystore, String password) {
        progress.postValue(true);
        compositeDisposable.add(importWalletInteract
                .importKeystore(keystore, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onError));
    }

    @Override
    public void onPrivateKey(String key) {
        progress.postValue(true);
        compositeDisposable.add(importWalletInteract
                .importPrivateKey(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onError));
    }

    @Override
    public void onMnemonic(String mnemonic) {
        progress.postValue(true);
        compositeDisposable.add(walletValidationsInteract
                .isValidMnemonic(mnemonic)
                .flatMap(importWalletInteract::importMnemonic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onError));
    }

    @Override
    public void onWatchAddress(String address) {
        progress.postValue(true);
        compositeDisposable.add(walletValidationsInteract
                .isValidEthAddress(address)
                .flatMap(importWalletInteract::importWatchAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onWallet, this::onError));
    }

    @Override
    public LiveData<Wallet> wallet() {
        return wallet;
    }

    private void onWallet(Wallet wallet) {
        progress.postValue(false);
        this.wallet.postValue(wallet);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    @Override
    protected void onError(Throwable throwable) {
        Crashlytics.logException(throwable);
        if (throwable.getCause() != null && throwable.getCause() instanceof ServiceException) {
            error.postValue(((ServiceException) throwable.getCause()).error);

        } else if (throwable instanceof WalletAlreadyExistsException || throwable.getCause() instanceof WalletAlreadyExistsException) {
            error.postValue(new ErrorEnvelope(C.ErrorCode.WALLET_ALREADY_EXIST, null, throwable.getCause()));

        } else if (throwable instanceof WalletException) {
            error.postValue(new ErrorEnvelope(C.ErrorCode.WALLET, throwable.getMessage(), throwable));

        } else {
            error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null, throwable.getCause()));
            Log.d("SESSION", "Err", throwable);
        }
    }
}
