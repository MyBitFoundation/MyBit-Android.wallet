package com.wallet.crypto.mybitapp.ui.activity.gassettings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class GasSettingsViewModelImpl extends GasSettingsViewModel {

    public static final int SET_GAS_SETTINGS = 1;

    private GetSessionInteract getSessionInteract;

    private MutableLiveData<BigInteger> gasPrice = new MutableLiveData<>();
    private MutableLiveData<BigInteger> gasLimit = new MutableLiveData<>();
    private MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    GasSettingsViewModelImpl(GetSessionInteract getSessionInteract, NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.getSessionInteract = getSessionInteract;
        gasPrice.setValue(BigInteger.ZERO);
        gasLimit.setValue(BigInteger.ZERO);
    }

    @Override
    public void prepare() {
        compositeDisposable.add(getSessionInteract
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSession, this::onError));
    }

    @Override
    public MutableLiveData<BigInteger> gasPrice() {
        return gasPrice;
    }

    @Override
    public MutableLiveData<BigInteger> gasLimit() {
        return gasLimit;
    }

    @Override
    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    private void onSession(Session session) {
        defaultNetwork.setValue(session.networkInfo);
    }

    @Override
    public BigInteger networkFee() {
        if (gasPrice.getValue() == null || gasLimit.getValue() == null) {
            return BigInteger.ZERO;
        }
        return gasPrice.getValue().multiply(gasLimit.getValue());
    }
}
