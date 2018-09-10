package com.wallet.crypto.mybitapp.ui.fragment.pin;

import android.arch.lifecycle.LiveData;

import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;

public abstract class PinViewModel extends BaseViewModel {
    public abstract void prepare();

    public abstract void putPinCode(String pinCode, Boolean isOptioned);

    public abstract void removePinCode();

    public abstract void checkIsPassOptioned();

    public abstract void acceptPassCodeInteractPinCodeEnteredSuccessful();

    public abstract LiveData<Boolean> onClosePinDialog();

    public abstract LiveData<String> onSetPassCodeToView();

    public abstract LiveData<Boolean> onShowRemovingPinCompleteDialog();
}
