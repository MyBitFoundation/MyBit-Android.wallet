package com.wallet.crypto.mybitapp.ui.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class NoInternetConnectionBaseViewModel extends BaseViewModel {
    private final NetworkChangeReceiver networkChangeReceiver;
    private final MutableLiveData<Boolean> networkConnectionChanged = new MutableLiveData<>();

    public NoInternetConnectionBaseViewModel(NetworkChangeReceiver networkChangeReceiver) {
        this.networkChangeReceiver = networkChangeReceiver;
        prepareNetworkChangeReceiver();
    }

    private void prepareNetworkChangeReceiver() {
        compositeDisposable.add(networkChangeReceiver.onNetworkConnectionChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkConnectionChanged::postValue, t -> { }));
    }

    public void requestForChangingNetworkConnection(Context context) {
        networkChangeReceiver.requestForChangingNetworkConnection(context);
    }

    public LiveData<Boolean> onNetworkConnectionChanged() { return networkConnectionChanged; }
}
