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
import com.wallet.crypto.mybitapp.ui.widget.OnImportPrivateKeyListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportPrivateKeyFragment extends Fragment {

    private static final OnImportPrivateKeyListener dummyOnImportPrivateKeyListener = key -> { };

    public static ImportPrivateKeyFragment create() {
        return new ImportPrivateKeyFragment();
    }

    @BindView(R.id.private_key)
    EditText privateKey;

    @OnClick(R.id.import_action)
    void onImportClicked(View view) {
        privateKey.setError(null);
        String value = privateKey.getText().toString();

        if (TextUtils.isEmpty(value) || value.length() != 64) {
            privateKey.setError(getString(R.string.error_invalid_private_key));
        } else if (onImportPrivateKeyListener != null) {
            onImportPrivateKeyListener.onPrivateKey(privateKey.getText().toString());
        }
    }

    private OnImportPrivateKeyListener onImportPrivateKeyListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_import_private_key, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setOnImportPrivateKeyListener(OnImportPrivateKeyListener onImportPrivateKeyListener) {
        this.onImportPrivateKeyListener = onImportPrivateKeyListener == null
                ? dummyOnImportPrivateKeyListener
                : onImportPrivateKeyListener;
    }
}
