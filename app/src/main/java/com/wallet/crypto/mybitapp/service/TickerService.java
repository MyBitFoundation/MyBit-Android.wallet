package com.wallet.crypto.mybitapp.service;

import com.wallet.crypto.mybitapp.entity.Ticker;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface TickerService {

    Single<Ticker> fetchTickerPrice(String ticker);
}
