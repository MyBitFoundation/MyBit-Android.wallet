package com.wallet.crypto.mybitapp.util;

import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Dasha on 19.06.2018
 */
public class TextUtil {

    public static String getTextFromClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip()) {
            return clipboard.getPrimaryClip().getItemAt(0).getText().toString();
        }
        return "";
    }
}
