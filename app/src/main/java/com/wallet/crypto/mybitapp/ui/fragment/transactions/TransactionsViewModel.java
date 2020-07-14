package com.wallet.crypto.mybitapp.ui.fragment.transactions;

import androidx.lifecycle.LiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.ui.base.BaseViewModel;

public abstract class TransactionsViewModel extends BaseViewModel {
    public abstract void prepare();

    public abstract void fetchNextTransactionsBatch(int page);

    public abstract void fetchTransactions();

    public abstract void showDetails(Context context, Transaction transaction);

    public abstract LiveData<Session> session();

    public abstract LiveData<Transaction[]> transactions();

    public abstract LiveData<Transaction[]> transactionsBatch();
}
