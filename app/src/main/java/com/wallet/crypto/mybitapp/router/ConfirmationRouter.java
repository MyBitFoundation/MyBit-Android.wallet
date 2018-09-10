package com.wallet.crypto.mybitapp.router;


import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.ui.activity.confirmation.ConfirmationActivity;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ConfirmationRouter {
    public void open(Context context, String to, BigInteger amount, String contractAddress,
                     int decimals, String symbol, BigDecimal balance, boolean sendingTokens) {
        Intent intent = new Intent(context, ConfirmationActivity.class);
        intent.putExtra(C.EXTRA_TO_ADDRESS, to);
        intent.putExtra(C.EXTRA_AMOUNT, amount.toString());
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, contractAddress);
        intent.putExtra(C.EXTRA_DECIMALS, decimals);
        intent.putExtra(C.EXTRA_SYMBOL, symbol);
        intent.putExtra(C.EXTRA_SENDING_TOKENS, sendingTokens);
        intent.putExtra(C.EXTRA_BALANCE, balance.toString());
        context.startActivity(intent);
    }
}
