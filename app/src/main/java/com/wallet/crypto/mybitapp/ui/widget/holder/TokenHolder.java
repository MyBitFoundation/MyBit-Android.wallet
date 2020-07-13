package com.wallet.crypto.mybitapp.ui.widget.holder;

import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Balance;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.entity.TokenInfo;
import com.wallet.crypto.mybitapp.ui.widget.OnTokenClickListener;
import com.wallet.crypto.mybitapp.util.IconUtil;

import java.math.BigDecimal;

import butterknife.BindView;

public class TokenHolder extends BinderViewHolder<Token> implements View.OnClickListener {

    private static final String TITLE_FORMAT = "%1$s (%2$s)";
    private static final String AMOUNT_FORMAT = "%1$s %2$s";

    @BindView(R.id.iv_token)
    ImageView ivToken;

    @BindView(R.id.tv_token_title)
    TextView tvTokenTitle;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.tv_amount_usd)
    TextView tvAmountUsd;

    private Token token;
    private OnTokenClickListener onTokenClickListener;

    private @ColorInt
    int colorPositive;

    private @ColorInt
    int colorNegative;

    public TokenHolder(int resId, ViewGroup parent) {
        super(resId, parent);
        itemView.setOnClickListener(this);
        colorPositive = ResourcesCompat.getColor(parent.getResources(), R.color.colorPositive, null);
        colorNegative = ResourcesCompat.getColor(parent.getResources(), R.color.colorNegative, null);
    }

    @Override
    public void bind(@Nullable Token data, @NonNull Bundle addition) {
        this.token = data;
        if (data == null) {
            fillEmpty();
            return;
        }
        try {
            TokenInfo tokenInfo = data.getTokenInfo();
            Balance balance = data.getBalance();
            ivToken.setImageResource(IconUtil.getTokenIcon(tokenInfo.symbol));
            tvTokenTitle.setText(String.format(TITLE_FORMAT, tokenInfo.name, tokenInfo.symbol));
            tvAmount.setText(balance.cryptoBalance.stripTrailingZeros().toPlainString());
            updateUsdBalance(balance, tvAmountUsd);
        } catch (Exception e) {
            fillEmpty();
        }
    }

    private void updateUsdBalance(Balance balance, TextView amountView) {
        BigDecimal usdPrice = balance.usdPrice;
        BigDecimal percentChange = balance.percentChange24h;

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(String.format(AMOUNT_FORMAT, usdPrice.stripTrailingZeros().toPlainString(), C.USD_SYMBOL));
        if (percentChange.compareTo(BigDecimal.ZERO) != 0 && usdPrice.compareTo(BigDecimal.ZERO) > 0) {
            addPercentChanges(builder, percentChange);
            amountView.setVisibility(View.VISIBLE);
            amountView.setText(builder);

        } else {
            amountView.setVisibility(View.GONE);
        }
    }

    private void addPercentChanges(SpannableStringBuilder builder, BigDecimal percentChange) {
        builder.append(" ");

        int spanStart = builder.length();

        boolean isPositive = percentChange.compareTo(BigDecimal.ZERO) > 0;
        String percentChangeFromRes = getString(isPositive ? R.string.percent_change_positive : R.string.percent_change_negative);
        String percentChangeString = String.format(percentChangeFromRes, percentChange.stripTrailingZeros().toPlainString());
        builder.append(percentChangeString);

        int spanEnd = spanStart + percentChangeString.length();

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(isPositive ? colorPositive : colorNegative);
        builder.setSpan(colorSpan, spanStart, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private void fillEmpty() {
        tvAmount.setText(R.string.minus);
    }

    @Override
    public void onClick(View v) {
        if (onTokenClickListener != null) {
            onTokenClickListener.onTokenClick(v, token);
        }
    }

    public void setOnTokenClickListener(OnTokenClickListener onTokenClickListener) {
        this.onTokenClickListener = onTokenClickListener;
    }
}
