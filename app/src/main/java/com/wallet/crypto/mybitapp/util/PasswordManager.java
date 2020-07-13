package com.wallet.crypto.mybitapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class PasswordManager {
    private static final String legacyKey = "35TheTru5tWa11ets3cr3tK3y377123!";
    private static final String legacyIv = "8201va0184a0md8i";

    public PasswordManager() {
    }

    public static native String getKeyStringFromNative();

    public static native String getIvStringFromNative();

    public static void setPasswordLegacy(String address, String password, Context context) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec("35TheTru5tWa11ets3cr3tK3y377123!".getBytes("UTF-8"), "AES");
        IvParameterSpec iv = new IvParameterSpec("8201va0184a0md8i".getBytes("UTF-8"));
        byte[] encryptedPassword = encrypt(password, key, iv);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(address + "-pwd", Base64.encodeToString(encryptedPassword, 0));
        editor.commit();
    }

    public static void setPassword(String address, String password, Context context) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        SecretKey key = new SecretKeySpec(getKeyStringFromNative().getBytes("UTF-8"), "AES");
        IvParameterSpec iv = new IvParameterSpec(getIvStringFromNative().getBytes("UTF-8"));
        byte[] encryptedPassword = encrypt(password, key, iv);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(address + "-pwd", Base64.encodeToString(encryptedPassword, 0));
        editor.commit();
    }

    public static String getPassword(String address, Context context) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        byte[] encryptedPassword = Base64.decode(sharedPreferences.getString(address + "-pwd", (String)null), 0);
        SecretKey oldKey = new SecretKeySpec("35TheTru5tWa11ets3cr3tK3y377123!".getBytes("UTF-8"), "AES");
        IvParameterSpec oldIv = new IvParameterSpec("8201va0184a0md8i".getBytes("UTF-8"));

        try {
            String decryptedPassword = decrypt(encryptedPassword, oldKey, oldIv);
            return decryptedPassword;
        } catch (Exception var9) {
            Log.e("PASSMAN", var9.getMessage());
            SecretKey key = new SecretKeySpec(getKeyStringFromNative().getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(getIvStringFromNative().getBytes("UTF-8"));
            String decryptedPassword = decrypt(encryptedPassword, key, iv);
            return decryptedPassword;
        }
    }

    private static byte[] encrypt(String plainText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, key, iv);
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    private static String decrypt(byte[] cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, key, iv);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    private static IvParameterSpec generateRandomIv() {
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        return iv;
    }

    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        /*int outputKeyLength = true;*/
        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private static SecretKey generateKey(String keyPhrase) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(keyPhrase.toCharArray(), salt, 65536, 256);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = keyFac.generateSecret(spec);
        return key;
    }

    static {
        System.loadLibrary("native-lib");
    }
}
