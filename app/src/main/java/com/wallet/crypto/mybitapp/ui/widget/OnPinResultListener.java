package com.wallet.crypto.mybitapp.ui.widget;

public interface OnPinResultListener {
    void onComplete(String passcode);
    void onUnComplete();
    void onRemovingPinCompleteOk();
}
