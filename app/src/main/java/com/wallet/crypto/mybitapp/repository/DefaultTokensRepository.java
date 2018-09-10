package com.wallet.crypto.mybitapp.repository;

import com.wallet.crypto.mybitapp.BuildConfig;
import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.TokenInfo;

/**
 * Created by Dasha on 12.06.2018
 */
public class DefaultTokensRepository implements DefaultTokensRepositoryType {

    private final TokenInfo defaultMybitMainnetToken = new TokenInfo(
            BuildConfig.MYBIT_TOKEN_ADDRESS, C.MYBIT_TOKEN_NAME, C.MYB_SYMBOL, C.MYBIT_DECIMALS);
    private final TokenInfo defaultEthereumToken = new TokenInfo(
            C.ETHEREUM_ADDRESS, BuildConfig.NETWORK_NAME, C.ETH_SYMBOL, C.ETHER_DECIMALS);

    @Override
    public TokenInfo getMybitToken(NetworkInfo networkInfo) {
        return defaultMybitMainnetToken;
    }

    @Override
    public TokenInfo getEthereumToken(NetworkInfo networkInfo) {
        return defaultEthereumToken;
    }

    @Override
    public TokenInfo[] getTokens(NetworkInfo networkInfo) {
        return new TokenInfo[]{
                getEthereumToken(networkInfo),
                getMybitToken(networkInfo)
        };
    }
}
