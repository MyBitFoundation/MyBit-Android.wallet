package com.wallet.crypto.mybitapp.util;

import java.math.BigDecimal;

/**
 * Created by Dasha on 15.06.2018
 */
public class DecimalUtil {

    public static String formatDot(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString().replaceAll("\\.", ",");
    }

    public static String formatDot(String value) {
        try {
            BigDecimal decimalValue = new BigDecimal(value);
            return decimalValue.stripTrailingZeros().toPlainString().replaceAll("\\.", ",");
        } catch (Exception ex) {
            return value;
        }
    }

    //BigDecimal.stripTrailingZeros() not working for zero
    public static BigDecimal stripZerosForZero(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            value = BigDecimal.ZERO;
        }
        return value.stripTrailingZeros();
    }
}
