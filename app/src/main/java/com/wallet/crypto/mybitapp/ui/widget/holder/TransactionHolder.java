package com.wallet.crypto.mybitapp.ui.widget.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.TransactionOperation;
import com.wallet.crypto.mybitapp.ui.widget.OnTransactionClickListener;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;

import static com.wallet.crypto.mybitapp.C.ETHER_DECIMALS;

public class TransactionHolder extends BinderViewHolder<Transaction> implements View.OnClickListener {

    public static final int VIEW_TYPE = 1003;

    private static final int SIGNIFICANT_FIGURES = 3;

    public static final String DEFAULT_ADDRESS_ADDITIONAL = "default_address";
    public static final String DEFAULT_SYMBOL_ADDITIONAL = "network_symbol";
    private static final String TRANSACTION_ZERO_FORMAT = "0 %s";
    private static final String TRANSACTION_SENT_FORMAT = "-%1$s %2$s";
    private static final String TRANSACTION_RECEIVE_FORMAT = "+%1$s %2$s";
    private static final String DEFAULT_ADDRESS = "0x";

    @BindView(R.id.type)
    TextView type;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.value)
    TextView value;

    @BindView(R.id.type_icon)
    ImageView typeIcon;

    private Transaction transaction;
    private String defaultAddress;
    private OnTransactionClickListener onTransactionClickListener;

    public TransactionHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(@Nullable Transaction data, @NonNull Bundle addition) {
        transaction = data; // reset
        if (this.transaction == null) {
            return;
        }
        defaultAddress = addition.getString(DEFAULT_ADDRESS_ADDITIONAL);

        String networkSymbol = addition.getString(DEFAULT_SYMBOL_ADDITIONAL);
        // If operations include token transfer, display token transfer instead
        TransactionOperation operation = transaction.operations == null
                || transaction.operations.length == 0 ? null : transaction.operations[0];

        if (operation == null || operation.contract == null) {
            // default to ether transaction
            fill(transaction.error, transaction.from, transaction.to, networkSymbol, transaction.value,
                    ETHER_DECIMALS);
        } else {
            fill(transaction.error, operation.from, operation.to, operation.contract.symbol, operation.value,
                    operation.contract.decimals);
        }
    }

    private void fill(
            String error,
            String from,
            String to,
            String symbol,
            String valueStr,
            int decimals) {
        boolean isSent = from.toLowerCase().equalsIgnoreCase(defaultAddress);
        boolean isError = !TextUtils.isEmpty(error);
        String title = getString(isError ? R.string.failed : isSent ? R.string.sent : R.string.received);
        type.setText(title);
        if (isError) {
            typeIcon.setImageResource(R.drawable.ic_fail);
        } else if (isSent) {
            typeIcon.setImageResource(R.drawable.ic_send);
        } else {
            typeIcon.setImageResource(R.drawable.ic_receive);
        }
        String toAddr = TextUtils.isEmpty(to) ? getContext().getString(R.string.title_contract_creation) : to;


        address.setText(isSent ? toAddr : from);


        value.setTextColor(ContextCompat.getColor(getContext(), isSent ? R.color.colorNegative : R.color.colorPositive));

        if (valueStr.equals(BigDecimal.ZERO.toString())) {
            valueStr = String.format(TRANSACTION_ZERO_FORMAT, symbol);
        } else {
            valueStr = isSent
                    ? String.format(TRANSACTION_SENT_FORMAT, getScaledValue(valueStr, decimals), symbol)
                    : String.format(TRANSACTION_RECEIVE_FORMAT, getScaledValue(valueStr, decimals), symbol);
        }

        this.value.setText(valueStr);
    }

    private String getScaledValue(String valueStr, int decimals) {
        // Perform decimal conversion
        BigDecimal value = new BigDecimal(valueStr);
        value = value.movePointLeft(decimals);
        return value.setScale(C.TRANSACTIONS_PRECISION, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    @Override
    public void onClick(View view) {
        if (onTransactionClickListener != null) {
            onTransactionClickListener.onTransactionClick(view, transaction);
        }
    }

    public void setOnTransactionClickListener(OnTransactionClickListener onTransactionClickListener) {
        this.onTransactionClickListener = onTransactionClickListener;
    }
}
