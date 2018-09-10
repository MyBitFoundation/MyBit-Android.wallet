package com.wallet.crypto.mybitapp.entity;

import com.google.gson.annotations.SerializedName;

public class Ticker {
    public String id;
    public String name;
    public String symbol;
    public String price;
    @SerializedName("percent_change_24h")
    public String percentChange24h;

    public Ticker(String id, String name, String symbol, String price, String percentChange24h) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.percentChange24h = percentChange24h;
    }

    public static Ticker getEmpty() {
        return new Ticker("", "", "", "0", "");
    }
}
