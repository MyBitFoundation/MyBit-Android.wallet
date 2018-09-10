package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.entity.Wallet;

public interface PreferenceRepositoryType {

    Wallet getDefaultWallet();

    void setDefaultWallet(Wallet wallet);

    void clearDefaultWallet();

    GasSettings getGasSettings(boolean forTokenTransfer);

    void setGasSettings(GasSettings gasPrice);

}
