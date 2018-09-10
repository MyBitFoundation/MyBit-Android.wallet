package com.wallet.crypto.mybitapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dasha on 22.06.2018
 */
public class DateUtil {

    private static String dateFormat = "dd MMMM yyyy, HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());

    public static String getDate(long timeStampInSec) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(TimeUnit.SECONDS.toMillis(timeStampInSec));
        return simpleDateFormat.format(cal.getTime());
    }
}
