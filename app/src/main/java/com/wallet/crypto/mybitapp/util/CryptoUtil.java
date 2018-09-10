package com.wallet.crypto.mybitapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtil {
    private static final String ENCRIPT_ALGORITHM = "SHA-256";
    private static final int MINIMAL_H_LENGTH = 2;

    public static String convertStringToMd5String(String inputString) {
        String encryptedString = "";

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(ENCRIPT_ALGORITHM);
            digest.update(inputString.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = getStringBuilder(messageDigest);
            encryptedString = hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return encryptedString;
    }

    private static StringBuilder getStringBuilder(byte[] messageDigest) {
        StringBuilder hexString = new StringBuilder();

        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);

            while (h.length() < MINIMAL_H_LENGTH) {
                h = "0" + h;
            }

            hexString.append(h);
        }

        return hexString;
    }
}
