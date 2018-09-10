package com.wallet.crypto.mybitapp.router;


import android.app.Activity;
import android.content.Intent;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.GasSettings;
import com.wallet.crypto.mybitapp.ui.activity.gassettings.GasSettingsActivity;
import com.wallet.crypto.mybitapp.ui.activity.gassettings.GasSettingsViewModelImpl;

public class GasSettingsRouter {
    public void open(Activity context, GasSettings gasSettings) {
        Intent intent = new Intent(context, GasSettingsActivity.class);
        intent.putExtra(C.EXTRA_GAS_PRICE, gasSettings.gasPrice.toString());
        intent.putExtra(C.EXTRA_GAS_LIMIT, gasSettings.gasLimit.toString());
        context.startActivityForResult(intent, GasSettingsViewModelImpl.SET_GAS_SETTINGS);
    }
}
