package com.wallet.crypto.mybitapp.entity;

public class Token {

    private TokenInfo tokenInfo;

    private Balance balance;

    public Token(TokenInfo tokenInfo, Balance balance) {
        this.tokenInfo = tokenInfo;
        this.balance = balance;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof Token
                && tokenInfo.equals(((Token) obj).tokenInfo)
                && balance.equals(((Token) obj).balance);
    }
}
