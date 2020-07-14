package com.wallet.crypto.mybitapp.ui.fragment.pin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.interact.PassCodeInteract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PinViewModelImpl extends PinViewModel {
    private final PassCodeInteract passCodeInteract;

    private final MutableLiveData<Boolean> onClosePinDialog = new MutableLiveData<>();
    private final MutableLiveData<String> onSetPassCodeToView = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onShowRemovingPinCompleteDialog = new MutableLiveData<>();

    PinViewModelImpl(PassCodeInteract passCodeInteract) {
        this.passCodeInteract = passCodeInteract;
    }

    @Override
    public void prepare() {
        findDefaultPinHash();
    }

    private void findDefaultPinHash() {
        compositeDisposable.add(passCodeInteract
                .fetchPassHash()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSetPassCodeToView::postValue, this::onError)
        );
    }

    @Override
    public void putPinCode(String pinCode, Boolean isOptioned) {
        compositeDisposable.add(passCodeInteract
                .put(pinCode, isOptioned)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    onClosePinDialog.postValue(true);
                }, this::onError)
        );
    }

    @Override
    public void removePinCode() {
        compositeDisposable.add(passCodeInteract
                .remove()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> onShowRemovingPinCompleteDialog.postValue(true), this::onError)
        );
    }

    @Override
    public void checkIsPassOptioned() {
        compositeDisposable.add(passCodeInteract
                .checkIsOptioned()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(passCodeInteract::acceptCheckIsOptionedChanged, throwable -> { })
        );
    }

    @Override
    public void acceptPassCodeInteractPinCodeEnteredSuccessful() {
        passCodeInteract.acceptPinCodeEnteredSuccessful();
    }

    @Override
    public LiveData<Boolean> onClosePinDialog() {
        return onClosePinDialog;
    }

    @Override
    public LiveData<String> onSetPassCodeToView() {
        return onSetPassCodeToView;
    }

    @Override
    public LiveData<Boolean> onShowRemovingPinCompleteDialog() {
        return onShowRemovingPinCompleteDialog;
    }
}