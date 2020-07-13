package com.wallet.crypto.mybitapp.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.ui.widget.OnImportKeystoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportKeystoreFragment extends Fragment {
    private static final OnImportKeystoreListener dummyOnImportKeystoreListener = (k, p) -> { };

    public static ImportKeystoreFragment create() {
        return new ImportKeystoreFragment();
    }

    @BindView(R.id.keystore)
    EditText keystore;

    @BindView(R.id.password)
    EditText password;

    @OnClick(R.id.import_action)
    void onImportClicked(View view) {
        this.keystore.setError(null);
        String keystore = this.keystore.getText().toString();
        String password = this.password.getText().toString();

        if (TextUtils.isEmpty(keystore)) {
            this.keystore.setError(getString(R.string.error_field_required));
        } else if (onImportKeystoreListener != null) {
            onImportKeystoreListener.onKeystore(keystore, password);
        }
    }

    private OnImportKeystoreListener onImportKeystoreListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_import_keystore, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setOnImportKeystoreListener(OnImportKeystoreListener onImportKeystoreListener) {
        this.onImportKeystoreListener = onImportKeystoreListener == null
                ? dummyOnImportKeystoreListener
                : onImportKeystoreListener;
    }
}
