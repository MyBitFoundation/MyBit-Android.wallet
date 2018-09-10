package com.wallet.crypto.mybitapp.ui.activity.importwallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ProgressBar;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.ErrorEnvelope;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;
import com.wallet.crypto.mybitapp.ui.fragment.ImportKeystoreFragment;
import com.wallet.crypto.mybitapp.ui.fragment.ImportMnemonicFragment;
import com.wallet.crypto.mybitapp.ui.fragment.ImportPrivateKeyFragment;
import com.wallet.crypto.mybitapp.ui.fragment.ImportWatchFragment;
import com.wallet.crypto.mybitapp.ui.widget.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ImportWalletActivity extends NoInternetConnectionBaseActivity<ImportWalletViewModel> {

    private static final int KEYSTORE_FORM_INDEX = 0;
    private static final int PRIVATE_KEY_FORM_INDEX = 1;
    private static final int MNEMONIC_FORM_INDEX = 2;
    private static final int WATCH_FORM_INDEX = 3;

    private final List<Pair<String, Fragment>> pages = new ArrayList<>();

    @Inject
    ImportWalletViewModelFactory importWalletViewModelFactory;

    private Dialog dialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_import_wallet;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        toolbar();
        pages.add(KEYSTORE_FORM_INDEX, new Pair<>(getString(R.string.tab_keystore), ImportKeystoreFragment.create()));
        pages.add(PRIVATE_KEY_FORM_INDEX, new Pair<>(getString(R.string.tab_private_key), ImportPrivateKeyFragment.create()));
        pages.add(MNEMONIC_FORM_INDEX, new Pair<>(getString(R.string.tab_mnemonic), ImportMnemonicFragment.create()));
        pages.add(WATCH_FORM_INDEX, new Pair<>(getString(R.string.tab_watch), ImportWatchFragment.create()));
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), pages));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, importWalletViewModelFactory)
                .get(ImportWalletViewModel.class);
        viewModel.progress().observe(this, this::onProgress);
        viewModel.error().observe(this, this::onError);
        viewModel.wallet().observe(this, this::onWallet);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Fragment fragment = pages.get(KEYSTORE_FORM_INDEX).second;
        if (fragment != null && fragment instanceof ImportKeystoreFragment) {
            ((ImportKeystoreFragment) fragment).setOnImportKeystoreListener(viewModel);
        }

        fragment = pages.get(PRIVATE_KEY_FORM_INDEX).second;
        if (fragment != null && fragment instanceof ImportPrivateKeyFragment) {
            ((ImportPrivateKeyFragment) fragment).setOnImportPrivateKeyListener(viewModel);
        }

        fragment = pages.get(MNEMONIC_FORM_INDEX).second;
        if (fragment != null && fragment instanceof ImportMnemonicFragment) {
            ((ImportMnemonicFragment) fragment).setOnImportMnemonicListener(viewModel);
        }

        fragment = pages.get(WATCH_FORM_INDEX).second;
        if (fragment != null && fragment instanceof ImportWatchFragment) {
            ((ImportWatchFragment) fragment).setOnImportWatchAddressListener(viewModel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        hideDialog();
    }

    private void onWallet(Wallet wallet) {
        Intent result = new Intent();
        result.putExtra(C.Key.WALLET, wallet);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        String message = getString(R.string.error_import);
        if (errorEnvelope.code == C.ErrorCode.WALLET_ALREADY_EXIST) {
            message = getString(R.string.error_import_such_wallet_exist);

        } else if (!TextUtils.isEmpty(errorEnvelope.message)) {
            message = errorEnvelope.message;

        } else if (errorEnvelope.throwable != null && !TextUtils.isEmpty(errorEnvelope.throwable.getMessage())) {
            message = errorEnvelope.throwable.getMessage();
        }

        hideDialog();
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_error)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }

    private void onProgress(boolean shouldShowProgress) {
        hideDialog();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_handling)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
