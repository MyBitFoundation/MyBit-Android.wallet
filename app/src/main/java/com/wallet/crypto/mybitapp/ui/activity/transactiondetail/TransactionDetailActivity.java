package com.wallet.crypto.mybitapp.ui.activity.transactiondetail;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Transaction;
import com.wallet.crypto.mybitapp.entity.TransactionDetailsModel;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

import static com.wallet.crypto.mybitapp.C.Key.TRANSACTION;

public class TransactionDetailActivity extends NoInternetConnectionBaseActivity<TransactionDetailViewModel> {

    @Inject
    TransactionDetailViewModelFactory transactionDetailViewModelFactory;

    @BindView(R.id.amount)
    TextView tvAmount;

    @BindView(R.id.from)
    TextView tvFrom;

    @BindView(R.id.to)
    TextView tvTo;

    @BindView(R.id.gas_fee)
    TextView tvGasFee;

    @BindView(R.id.txn_hash)
    TextView tvTxnHash;

    @BindView(R.id.txn_time)
    TextView tvTxnTime;

    @BindView(R.id.block_number)
    TextView tvBlockNumber;

    @BindView(R.id.to_title)
    TextView toTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
    }

    @Override
    protected void onPrepareViewModel() {
        Transaction transaction = getIntent().getParcelableExtra(TRANSACTION);
        if (transaction == null) {
            finish();
            return;
        }
        toolbar();

        viewModel = ViewModelProviders.of(this, transactionDetailViewModelFactory)
                .get(TransactionDetailViewModel.class);
        viewModel.transactionDetails().observe(this, this::onTransactionDetails);
        viewModel.prepare(transaction);
    }

    private void onTransactionDetails(TransactionDetailsModel transactionDetails) {
        tvFrom.setText(transactionDetails.from);

        if (transactionDetails.to.isEmpty()) {
            toTitle.setText(R.string.title_contract_creation);
            toTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.big_text));
            tvTo.setVisibility(View.GONE);
        } else {
            tvTo.setText(transactionDetails.to);
        }

        tvGasFee.setText(transactionDetails.gasFee);
        tvTxnHash.setText(transactionDetails.txHash);
        tvTxnTime.setText(transactionDetails.txTime);
        tvBlockNumber.setText(transactionDetails.blockNumber);
        tvAmount.setTextColor(ContextCompat.getColor(this, transactionDetails.isSent ? R.color.red : R.color.green));
        tvAmount.setText(transactionDetails.value);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            viewModel.shareTransactionDetail(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.more_detail)
    public void onClick(View v) {
        viewModel.showMoreDetails(v.getContext());
    }
}
