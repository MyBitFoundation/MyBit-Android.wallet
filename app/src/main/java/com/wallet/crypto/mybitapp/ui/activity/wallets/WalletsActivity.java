package com.wallet.crypto.mybitapp.ui.activity.wallets;

import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;
import com.wallet.crypto.mybitapp.ui.widget.adapter.WalletsAdapter;
import com.wallet.crypto.mybitapp.ui.widget.view.AddWalletView;
import com.wallet.crypto.mybitapp.ui.widget.view.BackupView;
import com.wallet.crypto.mybitapp.ui.widget.view.BackupWarningView;
import com.wallet.crypto.mybitapp.ui.widget.view.SystemView;
import com.wallet.crypto.mybitapp.util.KeyboardUtils;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

import static com.wallet.crypto.mybitapp.C.IMPORT_REQUEST_CODE;
import static com.wallet.crypto.mybitapp.C.SHARE_REQUEST_CODE;

public class WalletsActivity extends NoInternetConnectionBaseActivity<WalletsViewModel> implements
        View.OnClickListener,
        AddWalletView.OnNewWalletClickListener,
        AddWalletView.OnImportWalletClickListener {

    @Inject
    WalletsViewModelFactory walletsViewModelFactory;

    @BindView(R.id.system_view)
    SystemView systemView;

    @BindView(R.id.backup_warning)
    BackupWarningView backupWarning;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.list)
    RecyclerView rvWallets;

    private WalletsAdapter adapter;
    private Dialog dialog;
    private boolean isSetDefault;
    private final Handler handler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_wallets;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
        initUiView();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, walletsViewModelFactory)
                .get(WalletsViewModel.class);

        viewModel.error().observe(this, this::onError);
        viewModel.progress().observe(this, systemView::showProgress);
        viewModel.wallets().observe(this, this::onFetchWallet);
        viewModel.noWallets().observe(this, this::onFetchWithoutWallet);
        viewModel.defaultWallet().observe(this, this::onChangeDefaultWallet);
        viewModel.createdWallet().observe(this, this::onCreatedWallet);
        viewModel.exportedStore().observe(this, this::openShareDialog);
        refreshLayout.setOnRefreshListener(viewModel::fetchWallets);
    }

    private void initUiView() {
        adapter = new WalletsAdapter(this::onSetWalletDefault, this::onDeleteWallet, this::onExportWallet);
        rvWallets.setLayoutManager(new LinearLayoutManager(this));
        rvWallets.setAdapter(adapter);
        systemView.attachRecyclerView(rvWallets);
        systemView.attachSwipeRefreshLayout(refreshLayout);
        backupWarning.setOnPositiveClickListener(this::onNowBackup);
    }

    private void onExportWallet(Wallet wallet) {
        showBackupDialog(wallet, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                onAddWallet();
            }
            break;
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_REQUEST_CODE) {
            showToolbar();
            showWalletImportedMessageIfNeed(resultCode);
        } else if (requestCode == SHARE_REQUEST_CODE) {
            shareRequestCode(resultCode);
        }
    }

    private void showWalletImportedMessageIfNeed(int resultCode) {
        if (resultCode == RESULT_OK) {
            viewModel.fetchWallets();
            Snackbar.make(systemView, getString(R.string.toast_message_wallet_imported), Snackbar.LENGTH_SHORT).show();

            if (adapter.getItemCount() < 1) {
                viewModel.showMain(this);
            }
        }
    }

    private void shareRequestCode(int resultCode) {
        if (resultCode == RESULT_OK) {
            Snackbar.make(systemView, getString(R.string.toast_message_wallet_exported), Snackbar.LENGTH_SHORT)
                    .show();
            backupWarning.hide();
            showToolbar();
            hideDialog();

            if (adapter.getItemCount() <= 1) {
                viewModel.showMain(this);
            }
        } else {
            dialog = buildDialog()
                    .setMessage(R.string.do_manage_make_backup)
                    .setPositiveButton(R.string.yes_continue, (dialog, which) -> {
                        hideDialog();
                        backupWarning.hide();
                        showToolbar();
                        if (adapter.getItemCount() <= 1) {
                            viewModel.showMain(this);
                        }
                    })
                    .setNegativeButton(R.string.no_repeat,
                            (dialog, which) -> {
                                openShareDialog(viewModel.exportedStore().getValue());
                                hideDialog();
                            })
                    .create();
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.try_again: {
                viewModel.fetchWallets();
            }
            break;
        }
    }

    @Override
    public void onNewWallet(View view) {
        hideDialog();
        viewModel.newWallet();
    }

    @Override
    public void onImportWallet(View view) {
        hideDialog();
        viewModel.importWallet(this);
    }

    private void onAddWallet() {
        AddWalletView addWalletView = new AddWalletView(this);
        addWalletView.setOnNewWalletClickListener(this);
        addWalletView.setOnImportWalletClickListener(this);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(addWalletView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void onChangeDefaultWallet(Wallet wallet) {
        if (isSetDefault) {
            onBackPressed();
        } else {
            adapter.setDefaultWallet(wallet);
        }
    }

    @Override
    public void onBackPressed() {
        if (systemView.isShowingEmpty()) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    private void onFetchWallet(Wallet[] wallets) {
        enableDisplayHomeAsUp();
        adapter.setWallets(wallets);
    }

    private void onFetchWithoutWallet(Object obj) {
        disableDisplayHomeAsUp();
        AddWalletView addWalletView = new AddWalletView(this, R.layout.layout_empty_add_account);
        addWalletView.setOnNewWalletClickListener(this);
        addWalletView.setOnImportWalletClickListener(this);
        systemView.showEmpty(addWalletView);
        adapter.setWallets(new Wallet[0]);
        hideToolbar();
    }

    private void onCreatedWallet(Wallet wallet) {
        hideToolbar();
        backupWarning.show(wallet);
        setResult(C.WALLET_CREATION_RESULT_CODE);
    }

    private void onLaterBackup(View view, Wallet wallet) {
        showNoBackupWarning(wallet);
    }

    private void onNowBackup(View view, Wallet wallet) {
        showBackupDialog(wallet, true);
    }

    private void showNoBackupWarning(Wallet wallet) {
        dialog = buildDialog()
                .setTitle(getString(R.string.title_dialog_watch_out))
                .setMessage(getString(R.string.dialog_message_unrecoverable_message))
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(R.string.i_understand, (dialog, whichButton) -> {
                    backupWarning.hide();
                    showToolbar();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void showBackupDialog(Wallet wallet, boolean isNew) {
        BackupView view = new BackupView(this);
        dialog = buildDialog()
                .setView(view)
                .setPositiveButton(R.string.ok,
                        (dialogInterface, i) -> {
                            viewModel.exportWallet(wallet, view.getPassword());
                            KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
                        })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    if (isNew) {
                        onCreatedWallet(wallet);
                    }
                    KeyboardUtils.hideKeyboard(view.findViewById(R.id.password));
                })
                .setOnDismissListener(dialog -> KeyboardUtils.hideKeyboard(view.findViewById(R.id.password)))
                .create();
        dialog.show();
        handler.postDelayed(() -> {
            KeyboardUtils.showKeyboard(view.findViewById(R.id.password));
        }, 500);
    }

    private void openShareDialog(String jsonData) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Keystore");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, jsonData);
        startActivityForResult(
                Intent.createChooser(sharingIntent, "Share via"),
                SHARE_REQUEST_CODE);
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        systemView.showError(errorEnvelope.message, this);
    }

    private void onSetWalletDefault(Wallet wallet) {
        viewModel.setDefaultWallet(wallet);
        isSetDefault = true;
    }

    private void onDeleteWallet(Wallet wallet) {
        dialog = buildDialog()
                .setTitle(getString(R.string.title_delete_account))
                .setMessage(getString(R.string.confirm_delete_account))
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(android.R.string.yes, (dialog, btn) -> viewModel.deleteWallet(wallet))
                .setNegativeButton(android.R.string.no, null)
                .create();
        dialog.show();
    }

    private AlertDialog.Builder buildDialog() {
        hideDialog();
        return new AlertDialog.Builder(this);
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}