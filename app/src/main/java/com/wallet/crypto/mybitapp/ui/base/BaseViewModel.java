package com.wallet.crypto.mybitapp.ui.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.exception.ServiceException;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {

    protected final MutableLiveData<ErrorEnvelope> error = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> progress = new MutableLiveData<>();
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public LiveData<ErrorEnvelope> error() {
        return error;
    }

    public LiveData<Boolean> progress() {
        return progress;
    }

    protected void onError(Throwable throwable) {
        Crashlytics.logException(throwable);
        if (throwable.getCause() != null && throwable.getCause() instanceof ServiceException) {
            error.postValue(((ServiceException) throwable.getCause()).error);
        } else {
            error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null, throwable.getCause()));
            Log.d("SESSION", "Err", throwable);
        }
    }
}