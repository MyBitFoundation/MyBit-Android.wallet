package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.TokenInfo;

/**
 * Created by Dasha on 12.06.2018
 */
public interface DefaultTokensRepositoryType {

    TokenInfo getMybitToken(NetworkInfo networkInfo);

    TokenInfo getEthereumToken(NetworkInfo networkInfo);

    TokenInfo[] getTokens(NetworkInfo networkInfo);
}
