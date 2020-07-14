package com.wallet.crypto.mybitapp.util;

import androidx.annotation.DrawableRes;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;

/**
 * Created by Dasha on 18.06.2018
 */
public class IconUtil {

    public static @DrawableRes
    int getTokenIcon(String symbol) {
        if (symbol.equalsIgnoreCase(C.MYB_SYMBOL)) {
            return R.drawable.ic_mybit_in_circle;
        }

        return R.drawable.ic_eth_in_circle;
    }
}
