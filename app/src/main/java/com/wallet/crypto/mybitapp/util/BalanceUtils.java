package com.wallet.crypto.mybitapp.util;

import com.wallet.crypto.mybitapp.C;

import org.ethereum.geth.Address;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BalanceUtils {
    public static BigDecimal weiToEth(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }

    public static String weiToEth(BigInteger wei, int sigFig) throws Exception {
        BigDecimal eth = weiToEth(wei);
        BigDecimal eth_scaled = eth.setScale(sigFig, RoundingMode.HALF_UP);
        return eth_scaled.toString();
    }

    public static String ethToUsd(String priceUsd, String ethBalance) {
        BigDecimal usd = new BigDecimal(ethBalance).multiply(new BigDecimal(priceUsd));
        usd = usd.setScale(2, RoundingMode.CEILING);
        return usd.toString();
    }

    public static String ethToWei(String eth) {
        BigDecimal wei = new BigDecimal(eth).multiply(new BigDecimal(C.WEI_IN_ETH));
        return wei.toBigInteger().toString();
    }

    public static boolean isValidPrecision(String eth, int decimals) {
        String stripTrailedEth = new BigDecimal(eth).stripTrailingZeros().toPlainString();
        String[] strings = stripTrailedEth.split("\\.", 2);
        return strings.length <= 1 || strings[1].length() <= decimals;
    }

    public static boolean isValidAddress(String address) {
        try {
            new Address(address);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static BigDecimal weiToGweiBI(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI);
    }

    public static String weiToGwei(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.GWEI).toPlainString();
    }

    public static BigInteger gweiToWei(BigDecimal gwei) {
        return Convert.toWei(gwei, Convert.Unit.GWEI).toBigInteger();
    }

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @param decimals - decimal places used to convert to subunits
     * @return amount in subunits
     */
    public static BigInteger baseToSubunit(String baseAmountStr, int decimals) {
        assert(decimals >= 0);
        BigDecimal baseAmount = new BigDecimal(baseAmountStr);
        BigDecimal subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals));
        try {
            return subunitAmount.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            assert(false);
            return subunitAmount.toBigInteger();
        }
    }

    /**
     * @param subunitAmount - amouunt in subunits
     * @param decimals - decimal places used to convert subunits to base
     * @return amount in base units
     */
    public static BigDecimal subunitToBase(BigInteger subunitAmount, int decimals) {
        assert(decimals >= 0);
        return new BigDecimal(subunitAmount).divide(BigDecimal.valueOf(10).pow(decimals));
    }
}
