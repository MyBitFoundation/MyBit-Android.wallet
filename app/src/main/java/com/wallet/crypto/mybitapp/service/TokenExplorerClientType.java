package com.wallet.crypto.mybitapp.service;

import com.wallet.crypto.mybitapp.entity.Token;

import io.reactivex.Observable;

public interface TokenExplorerClientType {

    Observable<Token[]> fetch(String walletAddress);

}