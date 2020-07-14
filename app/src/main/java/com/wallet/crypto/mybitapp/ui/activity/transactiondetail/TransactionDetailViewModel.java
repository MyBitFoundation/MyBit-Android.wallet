package com.wallet.crypto.mybitapp.ui.activity.transactiondetail;

import androidx.lifecycle.LiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.TransactionDetailsModel;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseViewModel;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;

public abstract class TransactionDetailViewModel extends NoInternetConnectionBaseViewModel {
    public TransactionDetailViewModel(NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
    }

    public abstract void showMoreDetails(Context context);

    public abstract void shareTransactionDetail(Context context);

    public abstract LiveData<TransactionDetailsModel> transactionDetails();

    public abstract void prepare(Transaction transaction);
}
