package com.wallet.crypto.mybitapp.repository;

import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface PassCodeSource {
    Completable put(String passcode, Boolean isOptioned);
    Single<Map<String, Boolean>> fetch();
    Completable remove();
}
