package com.wallet.crypto.mybitapp.ui.activity.transactiondetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.TransactionDetailsModel;
import com.wallet.crypto.mybitapp.entity.TransactionOperation;
import com.wallet.crypto.mybitapp.interact.GetSessionInteract;
import com.wallet.crypto.mybitapp.router.ExternalBrowserRouter;
import com.wallet.crypto.mybitapp.router.ShareTransactionRouter;
import com.wallet.crypto.mybitapp.ui.widget.receiver.NetworkChangeReceiver;
import com.wallet.crypto.mybitapp.util.BalanceUtils;
import com.wallet.crypto.mybitapp.util.DateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class TransactionDetailViewModelImpl extends TransactionDetailViewModel {

    private static final String TRANSACTION_ZERO_FORMAT = "0 %s";
    private static final String TRANSACTION_SENT_FORMAT = "%1$s %2$s";
    private static final String TRANSACTION_RECEIVE_FORMAT = "+%1$s %2$s";

    private final GetSessionInteract getSessionInteract;
    private final ExternalBrowserRouter externalBrowserRouter;
    private final ShareTransactionRouter shareTransactionRouter;

    private final MutableLiveData<Session> session = new MutableLiveData<>();
    private final MutableLiveData<TransactionDetailsModel> transactionDetails = new MutableLiveData<>();

    private Transaction transaction;

    public TransactionDetailViewModelImpl(
            GetSessionInteract getSessionInteract,
            ExternalBrowserRouter externalBrowserRouter,
            ShareTransactionRouter shareTransactionRouter,
            NetworkChangeReceiver networkChangeReceiver) {
        super(networkChangeReceiver);
        this.getSessionInteract = getSessionInteract;
        this.externalBrowserRouter = externalBrowserRouter;
        this.shareTransactionRouter = shareTransactionRouter;
    }

    public void prepare(final Transaction transaction) {
        this.transaction = transaction;
        compositeDisposable.add(getSessionInteract
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    session.postValue(s);

                    BigInteger gasFee = new BigInteger(transaction.gasUsed).multiply(new BigInteger(transaction.gasPrice));
                    BigDecimal gasEth = BalanceUtils.weiToEth(gasFee);

                    TransactionOperation operation = transaction.operations == null || transaction.operations.length == 0
                            ? null
                            : transaction.operations[0];
                    String from = transaction.from;
                    String to = transaction.to;
                    if (operation != null && operation.contract != null) {
                        from = operation.from;
                        to = operation.to;
                    }

                    boolean isSent = from.toLowerCase().equals(s.wallet.address);
                    boolean isEmptyOperations = transaction.operations == null || transaction.operations.length == 0;

                    String rawValue = getValue(isEmptyOperations);
                    String symbol = getSymbol(isEmptyOperations, s.networkInfo);
                    int decimals = getDecimals(isEmptyOperations);

                    String formattedValue = formatValue(rawValue, symbol, isSent, decimals);
                    //String contractCreation = context

                    TransactionDetailsModel transactionDetailsModel = new TransactionDetailsModel(
                            from,
                            to,
                            gasEth.toPlainString(),
                            transaction.hash,
                            DateUtil.getDate(transaction.timeStamp),
                            transaction.blockNumber,
                            isSent,
                            formattedValue
                    );

                    transactionDetails.postValue(transactionDetailsModel);
                }));
    }

    private String getValue(boolean isEmptyOperations) {
        return isEmptyOperations
                ? transaction.value
                : transaction.operations[0].value;
    }

    private String getSymbol(boolean isEmptyOperations, NetworkInfo networkInfo) {
        return isEmptyOperations
                ? networkInfo == null ? "" : networkInfo.symbol
                : transaction.operations[0].contract.symbol;
    }

    private int getDecimals(boolean isEmptyOperations) {
        return isEmptyOperations
                ? C.ETHER_DECIMALS
                : transaction.operations[0].contract.decimals;
    }

    private String formatValue(String rawValue, String symbol, boolean isSent, int decimals) {
        if (rawValue.equals(BigDecimal.ZERO.toString())) {
            return String.format(TRANSACTION_ZERO_FORMAT, symbol);
        } else {
            return isSent
                    ? String.format(TRANSACTION_SENT_FORMAT, getScaledValue(rawValue, decimals), symbol)
                    : String.format(TRANSACTION_RECEIVE_FORMAT, getScaledValue(rawValue, decimals), symbol);
        }
    }

    private String getScaledValue(String valueStr, int decimals) {
        // Perform decimal conversion
        BigDecimal value = new BigDecimal(valueStr);
        value = value.movePointLeft(decimals);
        return value.stripTrailingZeros().toPlainString();
    }

    public LiveData<TransactionDetailsModel> transactionDetails() {
        return transactionDetails;
    }

    public void showMoreDetails(Context context) {
        if (session.getValue() != null) {
            externalBrowserRouter.open(context, transaction, session.getValue().networkInfo);
        }
    }

    @Override
    public void shareTransactionDetail(Context context) {
        if (session.getValue() != null) {
            shareTransactionRouter.open(context, transaction, session.getValue().networkInfo);
        }

    }
}
