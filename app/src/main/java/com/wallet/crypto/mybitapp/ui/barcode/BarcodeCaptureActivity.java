package com.wallet.crypto.mybitapp.ui.barcode;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.wallet.crypto.mybitapp.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public final class BarcodeCaptureActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    private static final int RC_HANDLE_CAMERA_PERM = 1023;
    private ZXingScannerView mScannerView;
    public static final String QR_CODE_RESULT = "QrCode";

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            mScannerView.startCamera();
        } else {
            requestCameraPermission();
        }
    }

    // Handles the requesting of the camera permission.
    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
        } else {
            showPermissionsError();
        }
    }

    // Restarts the camera
    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
    }

    // Stops the camera
    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = new Intent();
        intent.putExtra(QR_CODE_RESULT, rawResult.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerView.startCamera();
            return;
        }

        showPermissionsError();
    }

    private void showPermissionsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.qr_code_title)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, (dialog, id) -> finish())
                .show();
    }
}
