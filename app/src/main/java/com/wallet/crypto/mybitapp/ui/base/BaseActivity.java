package com.wallet.crypto.mybitapp.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.fragment.pin.PinFragment;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity<T extends BaseViewModel> extends DaggerAppCompatActivity {
    private PinFragment pinFragment;
    private Snackbar noConnectionSnackBar;

    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initNoConnectionSnackbar();
        onPrepareViewModel();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (layoutResID != 0) {
            super.setContentView(getContentView());
            ButterKnife.bind(this);
        }
    }

    protected abstract void onPrepareViewModel();

    protected abstract void onRemovedPinFragment();

    @LayoutRes
    protected abstract int getContentView();

    protected Toolbar toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
        }
        enableDisplayHomeAsUp();
        return toolbar;
    }

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void enableDisplayHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void disableDisplayHomeAsUp() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    protected void hideToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    protected void showToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    protected void replaceFragment(int replaceId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction(replaceId, fragment);
        fragmentTransaction.commit();
    }

    private FragmentTransaction getFragmentTransaction(int replaceId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (!fragment.isAdded()) {
            fragmentTransaction.replace(replaceId, fragment);
        }

        return fragmentTransaction;
    }

    protected PinFragment getPinFragment() {
        if (pinFragment != null) {
            return pinFragment;
        } else {
            return pinFragment = new PinFragment();
        }
    }

    protected void showPin(Boolean cancelBackPress, Boolean justCheckPass, Boolean removePass) {
        pinFragment = getPinFragment();

        if (!pinFragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putBoolean(C.EXTRA_CANCEL_BACK, cancelBackPress);
            bundle.putBoolean(C.EXTRA_ONLY_CHECK_PASS, justCheckPass);
            bundle.putBoolean(C.EXTRA_REMOVE_PASS, removePass);
            pinFragment.setArguments(bundle);
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_out_left, R.anim.slide_in_right);
            transaction.addToBackStack(getPackageName().getClass().getName());
            transaction.add(android.R.id.content, pinFragment);
            transaction.commit();
        }
    }

    protected void removePinFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.popBackStack();
        onRemovedPinFragment();
    }

    @Override
    public void onBackPressed() {
        if (getPinFragment().isAdded()) {
            getPinFragment().onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void showNotInternetConnection() {
        noConnectionSnackBar.show();
    }

    public void hideNotInternetConnection() {
        noConnectionSnackBar.dismiss();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof PinFragment) {
            hideNotInternetConnection();
        }
    }

    private void initNoConnectionSnackbar() {
        noConnectionSnackBar = Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.no_internet_connection),
                Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout snackbarLayout = initSnackbarLayout();
        initSnackbarTextView(snackbarLayout);
    }

    @NonNull
    private Snackbar.SnackbarLayout initSnackbarLayout() {
        View snackbarLayout = noConnectionSnackBar.getView();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarLayout.getLayoutParams();
        int bottomMargin = getResources().getDimensionPixelOffset(R.dimen.no_internet_popup_margin_bottom);
        int horizontalMargin = getResources().getDimensionPixelOffset(R.dimen.no_internet_popup_margin_se);
        params.setMargins(horizontalMargin, 0, horizontalMargin, bottomMargin);
        snackbarLayout.setLayoutParams(params);

        snackbarLayout.setBackgroundResource(R.drawable.no_internet_connection_shape);
        snackbarLayout.setPadding(0, 0, 0, 0);
        ViewCompat.setElevation(snackbarLayout, 0f);

        return (Snackbar.SnackbarLayout) snackbarLayout;
    }

    private void initSnackbarTextView(Snackbar.SnackbarLayout snackbarLayout) {
        TextView textView = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(textView, R.style.NoInternetConnectionTextView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.no_internet_popup_shadow_bottom);
        textView.setLayoutParams(params);
    }
}
