package com.wallet.crypto.mybitapp.ui.widget.holder;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.widget.adapter.WalletsAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletHolder extends BinderViewHolder<Wallet> {

    public static final int VIEW_TYPE = 1001;
    public final static String IS_DEFAULT_ADDITION = "is_default";
    public static final String IS_LAST_ITEM = "is_last";

    @BindView(R.id.default_action)
    RadioButton defaultAction;

    @BindView(R.id.delete_action)
    ImageView deleteAction;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.export_action)
    ImageView exportAction;

    @BindView(R.id.iv_watch)
    ImageView ivWatch;

    private WalletsAdapter.OnSetWalletDefaultListener onSetWalletDefaultListener;
    private WalletsAdapter.OnWalletDeleteListener onWalletDeleteListener;
    private WalletsAdapter.OnExportWalletListener onExportWalletListener;
    private Wallet wallet;

    public WalletHolder(int resId, ViewGroup parent) {
        super(resId, parent);
    }

    @OnClick({R.id.address, R.id.default_action})
    public void setDefaultWallet() {
        if (onSetWalletDefaultListener != null) {
            onSetWalletDefaultListener.onSetDefault(wallet);
        }
    }

    @OnClick(R.id.delete_action)
    public void deleteWallet() {
        if (onWalletDeleteListener != null) {
            onWalletDeleteListener.onDelete(wallet);
        }
    }

    @OnClick(R.id.export_action)
    public void exportWallet() {
        if (onExportWalletListener != null) {
            onExportWalletListener.onExport(wallet);
        }
    }

    @Override
    public void bind(@Nullable Wallet data, @NonNull Bundle addition) {
        wallet = null;
        address.setText(null);
        defaultAction.setEnabled(false);

        if (data == null) {
            return;
        }

        this.wallet = data;
        address.setText(wallet.address);
        defaultAction.setChecked(addition.getBoolean(IS_DEFAULT_ADDITION, false));
        defaultAction.setEnabled(true);
        boolean isWatch = wallet.type == Wallet.WATCH;
        exportAction.setVisibility(isWatch ? View.GONE : View.VISIBLE);
        ivWatch.setVisibility(isWatch ? View.VISIBLE : View.GONE);
    }

    public void setOnSetWalletDefaultListener(WalletsAdapter.OnSetWalletDefaultListener onSetWalletDefaultListener) {
        this.onSetWalletDefaultListener = onSetWalletDefaultListener;
    }

    public void setOnWalletDeleteListener(WalletsAdapter.OnWalletDeleteListener onWalletDeleteListener) {
        this.onWalletDeleteListener = onWalletDeleteListener;
    }

    public void setOnExportWalletListener(WalletsAdapter.OnExportWalletListener onExportWalletListener) {
        this.onExportWalletListener = onExportWalletListener;
    }
}
