package com.wallet.crypto.mybitapp.ui.fragment.wallet;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;

import java.math.BigDecimal;

public abstract class WalletViewModel extends BaseViewModel {
    public abstract void prepare();

    public abstract LiveData<BigDecimal> totalBalance();

    public abstract LiveData<Token[]> tokens();

    public abstract void fetchTokens();

    public abstract void showSend(Context context, String contractAddress);

    public abstract void showSendToken(Context context, String address, String symbol, int decimals,
                                       BigDecimal balance);

    public abstract void showMyAddress(Context context);
}
