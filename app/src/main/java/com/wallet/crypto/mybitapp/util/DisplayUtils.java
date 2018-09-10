package com.wallet.crypto.mybitapp.util;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayUtils {
    public static int getPxFromDp(DisplayMetrics displayMetrics, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }
}
