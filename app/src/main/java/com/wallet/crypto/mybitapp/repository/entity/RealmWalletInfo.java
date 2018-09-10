package com.wallet.crypto.mybitapp.repository.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dasha on 15.06.2018
 */
public class RealmWalletInfo extends RealmObject {

    public static final String COL_ADDRESS = "address";
    public static final String COL_TYPE = "type";

    @PrimaryKey
    private String address;
    private String data;
    private int type;

    public RealmWalletInfo(){

    }

    public RealmWalletInfo(String address, int type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
