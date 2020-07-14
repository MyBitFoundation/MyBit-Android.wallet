package com.wallet.crypto.mybitapp.ui.activity.receive;

import androidx.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wallet.crypto.mybitapp.R;
import com.wallet.crypto.mybitapp.entity.NetworkInfo;
import com.wallet.crypto.mybitapp.entity.Wallet;
import com.wallet.crypto.mybitapp.ui.base.NoInternetConnectionBaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Func0;

import static com.wallet.crypto.mybitapp.C.Key.WALLET;

public class ReceiveActivity extends NoInternetConnectionBaseActivity<ReceiveViewModel> {
    private static final float QR_IMAGE_WIDTH_RATIO = 0.9f;
    public static final String KEY_ADDRESS = "key_address";

    @Inject
    ReceiveViewModelFactory receiveViewModelFactory;

    private Wallet wallet;

    @BindView(R.id.address_suggestion)
    TextView addressSuggestion;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.qr_image)
    ImageView qrImage;

    @BindView(R.id.pb_qr_image)
    ProgressBar pbQrImage;

    private Disposable disposable;

    @OnClick(R.id.copy_action)
    void onCopyClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(KEY_ADDRESS, wallet.address);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_receive;
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
        viewModel = ViewModelProviders.of(this, receiveViewModelFactory)
                .get(ReceiveViewModel.class);
    }

    private void deferQrImageCreatingObservable() {
        disposable = Observable.defer(
                (Func0<ObservableSource<? extends Bitmap>>) () -> {
                    try {
                        final Bitmap bitmapQrCode = createQRImage(wallet.address);
                        return Observable.just(bitmapQrCode);
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showQrBitmap, t -> showErrorQrCodeToast());
    }

    protected void showQrBitmap(Bitmap qrBitmap) {
        qrImage.setVisibility(View.VISIBLE);
        qrImage.setImageBitmap(qrBitmap);
        pbQrImage.setVisibility(View.GONE);
    }

    protected void showErrorQrCodeToast() {
        Toast.makeText(this, getString(R.string.error_fail_generate_qr), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        deferQrImageCreatingObservable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }

    private void initUiView() {
        wallet = getIntent().getParcelableExtra(WALLET);
        NetworkInfo networkInfo = viewModel.getNetworkInfo();
        String suggestion = getString(R.string.suggestion_this_is_your_address, networkInfo.name);
        addressSuggestion.setText(suggestion);
        address.setText(wallet.address);
    }

    private Bitmap createQRImage(String address) {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int imageSize = (int) (size.x * QR_IMAGE_WIDTH_RATIO);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    address,
                    BarcodeFormat.QR_CODE,
                    imageSize,
                    imageSize,
                    null);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error_fail_generate_qr), Toast.LENGTH_SHORT)
                    .show();
        }
        return null;
    }
}
