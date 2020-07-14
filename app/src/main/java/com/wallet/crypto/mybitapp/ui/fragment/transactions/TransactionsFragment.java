package com.wallet.crypto.mybitapp.ui.fragment.transactions;

import android.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Session;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.ui.base.BaseNavigationFragment;
import com.wallet.crypto.mybitapp.ui.widget.EndlessRecyclerViewScrollListener;
import com.wallet.crypto.mybitapp.ui.widget.adapter.TransactionsAdapter;
import com.wallet.crypto.mybitapp.util.RootUtil;

import javax.inject.Inject;

import butterknife.BindView;

public class TransactionsFragment extends BaseNavigationFragment<TransactionsViewModel> {
    @Inject
    TransactionsViewModelFactory transactionsViewModelFactory;

    @BindView(R.id.rv_transactions)
    RecyclerView rvTransactions;

    @BindView(R.id.refresh_transactions)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_box)
    Group emptyGroup;

    private TransactionsAdapter adapter;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static TransactionsFragment create() {
        return new TransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportFragmentInjector().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_transactions;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TransactionsAdapter(this::onTransactionClick);
        rvTransactions.setAdapter(adapter);
        rvTransactions.setItemAnimator(null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTransactions.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                viewModel.fetchNextTransactionsBatch(page);
            }
        };
        rvTransactions.addOnScrollListener(scrollListener);
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, transactionsViewModelFactory)
                .get(TransactionsViewModel.class);
        viewModel.progress().observe(this, this::showProgress);
        viewModel.error().observe(this, this::onError);
        viewModel.transactions().observe(this, this::onTransactions);
        viewModel.transactionsBatch().observe(this, this::onTransactionsBatch);
        viewModel.session().observe(this, this::onSession);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.fetchTransactions();
        });
        viewModel.prepare();
    }

    private void onTransactionClick(View view, Transaction transaction) {
        viewModel.showDetails(view.getContext(), transaction);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRoot();
    }

    @Override
    public void reinitTitles() {
        setTitles(getString(R.string.navigation_transactions), "");
    }

    private void onTransactions(Transaction[] transactions) {
        scrollListener.resetState();
        boolean isEmpty = transactions == null || transactions.length == 0;
        adapter.addTransactions(transactions, true);
        showEmpty(isEmpty);
    }

    private void onTransactionsBatch(Transaction[] transactions) {
        adapter.addTransactions(transactions, false);
    }

    private void showProgress(boolean shouldShow) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(shouldShow);
        }
    }

    private void onSession(Session session) {
        if (session != null) {
            adapter.setDefaultWallet(session.wallet);
            adapter.setDefaultNetwork(session.networkInfo);
        }
    }

    private void showEmpty(boolean isEmpty) {
        emptyGroup.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvTransactions.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        showEmpty(true);
        Toast.makeText(getContext(), R.string.error_fail_load_transaction, Toast.LENGTH_SHORT).show();
    }

    private void checkRoot() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (RootUtil.isDeviceRooted() && pref.getBoolean("should_show_root_warning", true)) {
            pref.edit().putBoolean("should_show_root_warning", false).apply();
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.root_title)
                    .setMessage(R.string.root_body)
                    .setNegativeButton(R.string.ok, (dialog, which) -> {
                    })
                    .show();
        }
    }
}
