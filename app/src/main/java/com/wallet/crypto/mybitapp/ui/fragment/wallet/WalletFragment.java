package com.wallet.crypto.mybitapp.ui.fragment.wallet;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Token;
import com.wallet.crypto.mybitapp.ui.base.BaseNavigationFragment;
import com.wallet.crypto.mybitapp.ui.widget.OnTokenClickListener;
import com.wallet.crypto.mybitapp.ui.widget.adapter.TokensAdapter;

import java.math.BigDecimal;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletFragment extends BaseNavigationFragment<WalletViewModel> implements OnTokenClickListener {

    private static final String TOTAL_BALANCE_FORMAT = "%1$s%2$s";

    @Inject
    WalletViewModelFactory walletViewModelFactory;

    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layout_content)
    ViewGroup lytContent;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;

    @BindView(R.id.rv_tokens)
    RecyclerView rvTokens;

    @OnClick(R.id.btn_receive)
    void onReceiveClick() {
        viewModel.showMyAddress(getContext());
    }

    @OnClick(R.id.btn_send)
    void onSend() {
        viewModel.showSend(getContext(), C.ETHEREUM_ADDRESS);
    }

    private TokensAdapter tokensAdapter;

    public static WalletFragment create() {
        return new WalletFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        supportFragmentInjector().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tokens;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tokensAdapter = new TokensAdapter(this);
        rvTokens.setAdapter(tokensAdapter);
        swipeRefreshLayout.setOnRefreshListener(viewModel::fetchTokens);
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, walletViewModelFactory)
                .get(WalletViewModel.class);
        viewModel.progress().observe(this, this::showProgress);
        viewModel.error().observe(this, this::onError);
        viewModel.tokens().observe(this, this::onTokens);
        viewModel.totalBalance().observe(this, this::onBalanceChanged);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.prepare();
    }

    @Override
    public void reinitTitles() {
        setTitles(getString(R.string.navigation_wallet), "");
    }

    @Override
    public void onTokenClick(View view, Token token) {
        viewModel.showSendToken(getContext(),
                token.getTokenInfo().address,
                token.getTokenInfo().symbol,
                token.getTokenInfo().decimals,
                token.getBalance().cryptoBalance);
    }

    private void onTokens(Token[] tokens) {
        tokensAdapter.submitList(tokens);

        if (lytContent.getVisibility() != View.VISIBLE) {
            lytContent.setVisibility(View.VISIBLE);
            lytContent.animate().alpha(0f).alphaBy(1f).start();
        }
    }

    private void onBalanceChanged(BigDecimal balance) {
        tvTotalAmount.setText(String.format(TOTAL_BALANCE_FORMAT, balance.stripTrailingZeros().toPlainString(), C.USD_SYMBOL));
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        showProgress(false);
        Toast.makeText(getContext(), errorEnvelope.message, Toast.LENGTH_SHORT).show();
    }

    private void showProgress(boolean shouldShow) {
        if (shouldShow && swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            return;
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(shouldShow);
        }
    }
}