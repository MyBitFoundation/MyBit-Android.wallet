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
import com.wallet.crypto.mybitapp.ui.widget.OnImportMnemonicListener;
import com.wallet.crypto.mybitapp.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dasha on 08.06.2018
 */
public class ImportMnemonicFragment extends Fragment {

    private static final OnImportMnemonicListener dummyOnImportMnemonicListener = key -> { };

    @BindView(R.id.mnemonic)
    EditText etMnemonic;

    private OnImportMnemonicListener onImportMnemonicListener;

    public static ImportMnemonicFragment create() {
        return new ImportMnemonicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_import_mnemonic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.import_action)
    public void onImportActionClick() {
        etMnemonic.setError(null);
        String value = etMnemonic.getText().toString();

        if (TextUtils.isEmpty(value)) {
            etMnemonic.setError(getString(R.string.error_empty_key));
        } else if (onImportMnemonicListener != null) {
            onImportMnemonicListener.onMnemonic(value);
        }
    }

    @OnClick(R.id.btn_paste)
    public void paste() {
        if (getContext() != null) {
            etMnemonic.setText(TextUtil.getTextFromClipboard(getContext()));
        }
    }

    public void setOnImportMnemonicListener(OnImportMnemonicListener onImportMnemonicListener) {
        this.onImportMnemonicListener = onImportMnemonicListener == null
                ? dummyOnImportMnemonicListener
                : onImportMnemonicListener;
    }
}
