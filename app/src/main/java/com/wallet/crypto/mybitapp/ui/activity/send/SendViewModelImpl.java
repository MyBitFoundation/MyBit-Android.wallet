package com.wallet.crypto.mybitapp.ui.activity.send;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.router.ConfirmationRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;
import com.wallet.crypto.mybitapp.util.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SendViewModelImpl extends SendViewModel {
    private final ConfirmationRouter confirmationRouter;

    private final MutableLiveData<Object> validAddress = new MutableLiveData<>();
    private final MutableLiveData<Object> validAmount = new MutableLiveData<>();
    private final MutableLiveData<Object> errorInvalidAddress = new MutableLiveData<>();
    private final MutableLiveData<Object> errorInvalidAmount = new MutableLiveData<>();
    private final MutableLiveData<Object> errorAmountPrecisionEth = new MutableLiveData<>();
    private final MutableLiveData<Object> errorAmountPrecisionMybit = new MutableLiveData<>();

    SendViewModelImpl(ConfirmationRouter confirmationRouter, NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.confirmationRouter = confirmationRouter;
    }

    @Override
    public void tryOpenConfirmation(Context context, String toAddress, String amount, int decimals,
                                    String contractAddress, String symbol, boolean sendingTokens,
                                    BigDecimal balance) {
        if (checkValidData(toAddress, amount, decimals, symbol)) {
            BigInteger amountInSubunits = BalanceUtils.baseToSubunit(amount, decimals);
            openConfirmation(context, toAddress, amountInSubunits, contractAddress, decimals,
                    symbol, balance, sendingTokens);
        }
    }

    @Override
    public LiveData<Object> setValidAddress() {
        return validAddress;
    }

    @Override
    public LiveData<Object> setValidAmount() {
        return validAmount;
    }

    @Override
    public LiveData<Object> setErrorAmountPrecisionEth() {
        return errorAmountPrecisionEth;
    }

    @Override
    public LiveData<Object> setErrorAmountPrecisionMybit() {
        return errorAmountPrecisionMybit;
    }

    @Override
    public LiveData<Object> setErrorInvalidAddress() {
        return errorInvalidAddress;
    }

    @Override
    public LiveData<Object> setErrorInvalidAmount() {
        return errorInvalidAmount;
    }

    private void openConfirmation(Context context, String to, BigInteger amount,
                                  String contractAddress, int decimals, String symbol,
                                  BigDecimal balance, boolean sendingTokens) {
        confirmationRouter.open(context, to, amount, contractAddress, decimals, symbol, balance, sendingTokens);
    }

    private boolean checkValidData(String toAddress, String amount, int decimals, String symbol) {

        boolean validData = true;

        if (!isAddressValid(toAddress)) {
            errorInvalidAddress.postValue(null);
            validData = false;
        } else {
            validAddress.postValue(null);
        }

        if (!isValidAmount(amount)) {
            errorInvalidAmount.postValue(null);
            validData = false;

        } else if (!isValidAmountPrecision(amount, decimals)) {
            postErrorBySymbol(symbol);
            validData = false;

        } else {
            validAmount.postValue(null);
        }

        return validData;
    }

    private void postErrorBySymbol(String symbol) {
        if (checkIsEth(symbol)) {
            errorAmountPrecisionEth.postValue(null);
        } else {
            errorAmountPrecisionMybit.postValue(null);
        }
    }

    private boolean checkIsEth(String symbol) {
        return symbol.equalsIgnoreCase(C.ETH_SYMBOL);
    }

    private boolean isValidAmountPrecision(String amount, int decimals) {
        try {
            return BalanceUtils.isValidPrecision(amount, decimals);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isAddressValid(String address) {
        return BalanceUtils.isValidAddress(address);
    }

    private boolean isValidAmount(String eth) {
        try {
            return BalanceUtils.ethToWei(eth) != null;
        } catch (Exception ex) {
            return false;
        }
    }
}