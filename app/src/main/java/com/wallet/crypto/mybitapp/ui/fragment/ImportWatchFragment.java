package com.wallet.crypto.mybitapp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.barcode.BarcodeCaptureActivity;
import com.wallet.crypto.mybitapp.ui.widget.OnImportWatchAddressListener;
import com.wallet.crypto.mybitapp.util.QRURLParser;
import com.wallet.crypto.mybitapp.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dasha on 15.06.2018
 */
public class ImportWatchFragment extends Fragment {

    private static final int BARCODE_READER_REQUEST_CODE = 2;

    private static final OnImportWatchAddressListener dummyOnImportWatchAddressListener = key -> { };

    @BindView(R.id.address)
    EditText address;

    private OnImportWatchAddressListener onImportWatchAddressListener;

    public static ImportWatchFragment create() {
        return new ImportWatchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_import_watch, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.import_action)
    public void onImportWatchAddressClick() {
        address.setError(null);
        String value = address.getText().toString();

        if (TextUtils.isEmpty(value)) {
            address.setError(getString(R.string.error_field_required));
        } else if (onImportWatchAddressListener != null) {
            onImportWatchAddressListener.onWatchAddress(address.getText().toString());
        }
    }

    @OnClick(R.id.btn_paste)
    public void paste() {
        if (getContext() != null) {
            address.setText(TextUtil.getTextFromClipboard(getContext()));
        }
    }

    @OnClick(R.id.btn_scan)
    public void scan() {
        Intent intent = new Intent(getContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String qrCodeResult = data.getStringExtra(BarcodeCaptureActivity.QR_CODE_RESULT);
                    QRURLParser parser = QRURLParser.getInstance();
                    String extracted_address = parser.extractAddressFromQrString(qrCodeResult);
                    if (extracted_address == null) {
                        Toast.makeText(getContext(), R.string.toast_qr_code_no_address, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    address.setText(qrCodeResult);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setOnImportWatchAddressListener(OnImportWatchAddressListener onImportPrivateKeyListener) {
        this.onImportWatchAddressListener = onImportPrivateKeyListener == null
                ? dummyOnImportWatchAddressListener
                : onImportPrivateKeyListener;
    }

}
