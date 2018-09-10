package com.wallet.crypto.mybitapp.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public abstract class BaseFragment<T extends BaseViewModel> extends DaggerFragment {

    @LayoutRes
    protected abstract int getLayout();

    protected T viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onPrepareViewModel();
    }

    protected abstract void onPrepareViewModel();

    protected void removePinFragment() {
        getBaseActivity().removePinFragment();
    }

    protected void showPin(Boolean cancelBackPress, Boolean justCheckPass, Boolean removePass) {
        getBaseActivity().showPin(cancelBackPress, justCheckPass, removePass);
    }

    private BaseActivity getBaseActivity() {
        return  (BaseActivity) getActivity();
    }
}
