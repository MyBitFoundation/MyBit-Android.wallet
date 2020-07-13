package com.wallet.crypto.mybitapp.ui.fragment.settings;

import androidx.lifecycle.LiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;

public abstract class SettingsViewModel extends BaseViewModel {
    public abstract void prepare();

    public abstract void reInitDefaultWallet();

    public abstract void showManageWallets(Context context);

    public abstract void showSocial(Context context, String uri);

    public abstract void showContactUs(Context context);

    public abstract void rateUs(Context context);

    public abstract LiveData<Wallet> defaultWallet();

    public abstract LiveData<Boolean> pinIsOptioned();
}
