package com.wallet.crypto.mybitapp.ui.fragment.pin;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.wallet.crypto.mybitapp.C;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.base.BaseFragment;
import com.wallet.crypto.mybitapp.ui.widget.OnPinResultListener;
import com.wallet.crypto.mybitapp.ui.widget.view.PinView;

import javax.inject.Inject;

import butterknife.BindView;

public class PinFragment extends BaseFragment<PinViewModel> implements OnPinResultListener {
    private static final int LAYOUT_ID = R.layout.activity_pin;

    @BindView(R.id.pin_view)
    PinView pinView;

    @Inject
    PinViewModelFactory pinViewModelFactory;

    private Boolean isCancelBackPressed;
    private Boolean isOnlyCheck;
    private Boolean isRemovePass;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initIntentExtra();
        initUiView();
    }

    @Override
    protected void onPrepareViewModel() {
        viewModel = ViewModelProviders.of(this, pinViewModelFactory)
                .get(PinViewModel.class);
        viewModel.onClosePinDialog().observe(this, (shouldFinish) -> removePinFragment());
        viewModel.onSetPassCodeToView().observe(this, this::setPassCodeHashToView);
        viewModel.onShowRemovingPinCompleteDialog().observe(this, this::showRemovingPinCompleteDialog);
    }

    private void initIntentExtra() {
        isCancelBackPressed = getArguments().getBoolean(C.EXTRA_CANCEL_BACK, false);
        isOnlyCheck = getArguments().getBoolean(C.EXTRA_ONLY_CHECK_PASS, false);
        isRemovePass = getArguments().getBoolean(C.EXTRA_REMOVE_PASS, false);
    }

    private void initUiView() {
        pinView.setResultListener(this);
    }

    private void setPassCodeHashToView(String passHash) {
        pinView.setConfirmedPin(passHash);
    }

    private void showRemovingPinCompleteDialog(Boolean show) {
        pinView.showRemovingPinComplete();
    }

    public void onResume() {
        super.onResume();
        viewModel.prepare();
    }

    public void onBackPressed() {
        if (!isCancelBackPressed) {
            viewModel.checkIsPassOptioned();
            removePinFragment();
        }
    }

    private void closePinCode() {
        if (isRemovePass) {
            viewModel.removePinCode();
        } else {
            removePinFragment();
        }
    }

    @Override
    public void onComplete(String passcode) {
        if (isOnlyCheck) {
            closePinCode();
            viewModel.acceptPassCodeInteractPinCodeEnteredSuccessful();
        } else {
            viewModel.putPinCode(passcode, true);
        }
    }

    @Override
    public void onUnComplete() { }

    @Override
    public void onRemovingPinCompleteOk() {
        removePinFragment();
    }

    @Override
    protected int getLayout() {
        return LAYOUT_ID;
    }
}