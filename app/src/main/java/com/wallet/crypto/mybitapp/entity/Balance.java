package com.wallet.crypto.mybitapp.entity;

import java.math.BigDecimal;

/**
 * Created by Dasha on 13.06.2018
 */
public class Balance {

    public final BigDecimal cryptoBalance;
    public final BigDecimal usdPrice;
    public final BigDecimal percentChange24h;

    public Balance(BigDecimal cryptoBalance, BigDecimal usdPrice, BigDecimal percentChange24h) {
        this.cryptoBalance = cryptoBalance;
        this.usdPrice = usdPrice;
        this.percentChange24h = percentChange24h;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Balance
                && this.cryptoBalance.compareTo(((Balance) obj).cryptoBalance) == 0
                && this.usdPrice.compareTo(((Balance) obj).usdPrice) == 0
                && this.percentChange24h.compareTo(((Balance) obj).percentChange24h) == 0;
    }
}
