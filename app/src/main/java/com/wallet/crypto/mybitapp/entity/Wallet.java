package com.wallet.crypto.mybitapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Wallet implements Parcelable {

    public static final int KEY_STORE = 0;
    public static final int WATCH = 1;
    public static final int MNEMONIC = 2;
    public static final int UNKNOWN = -1;

    public final String address;

    public final int type;

    public Wallet(@NonNull String address) {
        this.address = address;
        this.type = UNKNOWN;
    }

    public Wallet(@NonNull String address, int i) {
        this.address = address;
        this.type = i;
    }

    private Wallet(Parcel in) {
        address = in.readString();
        type = in.readInt();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public boolean sameAddress(String address) {
        return this.address != null && this.address.equals(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeInt(type);
    }
}
