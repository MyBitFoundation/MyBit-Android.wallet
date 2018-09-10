package com.wallet.crypto.mybitapp.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.entity.Wallet;

import java.math.BigInteger;

public class SharedPreferenceRepository implements PreferenceRepositoryType {

    private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
    private static final String CURRENT_ACCOUNT_TYPE_KEY = "current_account_type";
    private static final String GAS_PRICE_KEY = "gas_price";
    private static final String GAS_LIMIT_KEY = "gas_limit";
    private static final String GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens";

    private final SharedPreferences pref;

    public SharedPreferenceRepository(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Wallet getDefaultWallet() {
        String address = this.pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
        if (TextUtils.isEmpty(address)) {
            return null;
        }
        return new Wallet(address, this.pref.getInt(CURRENT_ACCOUNT_TYPE_KEY, Wallet.UNKNOWN));
    }

    @Override
    public void setDefaultWallet(Wallet wallet) {
        pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, wallet.address).apply();
        pref.edit().putInt(CURRENT_ACCOUNT_TYPE_KEY, wallet.type).apply();
    }

    @Override
    public void clearDefaultWallet() {
        pref.edit().remove(CURRENT_ACCOUNT_ADDRESS_KEY).apply();
        pref.edit().remove(CURRENT_ACCOUNT_TYPE_KEY).apply();
    }

    @Override
    public GasSettings getGasSettings(boolean forTokenTransfer) {
        BigInteger gasPrice = new BigInteger(pref.getString(GAS_PRICE_KEY, C.DEFAULT_GAS_PRICE));
        BigInteger gasLimit = new BigInteger(pref.getString(GAS_LIMIT_KEY, C.DEFAULT_GAS_LIMIT));
        if (forTokenTransfer) {
            gasLimit = new BigInteger(pref.getString(GAS_LIMIT_FOR_TOKENS_KEY, C.DEFAULT_GAS_LIMIT_FOR_TOKENS));
        }

        return new GasSettings(gasPrice, gasLimit);
    }

    @Override
    public void setGasSettings(GasSettings gasSettings) {
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply();
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply();
    }
}
