package com.wallet.crypto.mybitapp.router;


import android.content.Context;
import android.content.Intent;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.ui.activity.send.SendActivity;

import java.math.BigDecimal;

public class SendTokenRouter {
    public void open(Context context, String address, String symbol, int decimals, BigDecimal balance) {
        Intent intent = new Intent(context, SendActivity.class);
        intent.putExtra(C.EXTRA_SENDING_TOKENS, true);
        intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, address);
        intent.putExtra(C.EXTRA_SYMBOL, symbol);
        intent.putExtra(C.EXTRA_DECIMALS, decimals);
        intent.putExtra(C.EXTRA_BALANCE, balance.toString());
        context.startActivity(intent);
    }
}
