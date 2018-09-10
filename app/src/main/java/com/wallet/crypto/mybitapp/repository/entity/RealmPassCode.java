package com.wallet.crypto.mybitapp.repository.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmPassCode extends RealmObject {
    @PrimaryKey
    private String passCodeHash;

    private Boolean isOptioned;

    public RealmPassCode() { }

    public RealmPassCode(String passCodeHash, Boolean isOptioned) {
        this.passCodeHash = passCodeHash;
        this.isOptioned = isOptioned;
    }

    public String getPassCodeHash() {
        return passCodeHash;
    }

    public void setPassCodeHash(String passCodeHash) {
        this.passCodeHash = passCodeHash;
    }

    public Boolean getOptioned() {
        return isOptioned;
    }

    public void setOptioned(Boolean optioned) {
        isOptioned = optioned;
    }
}
