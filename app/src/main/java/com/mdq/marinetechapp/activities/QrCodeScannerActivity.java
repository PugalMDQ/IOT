package com.mdq.marinetechapp.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mdq.marinetechapp.R;

import java.util.Objects;

public class QrCodeScannerActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    private CodeScannerView scanner_view;
   // OnQrScannerInterface mListener;
    private Bundle bundle;
    String sideAOrB ="";

    public interface OnQrScannerInterface {
        void onSuccessfulQrCodeScan(String result);
    }

//    public void setListener(OnQrScannerInterface listener) {
//        mListener = listener;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        initialisation();
        bundle = getIntent().getExtras();
        if (bundle!=null){
            sideAOrB = getIntent().getStringExtra("side");
        }
        codeScanner = new CodeScanner(this, scanner_view);
        codeScanner.setDecodeCallback(result -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("qrResult", result.getText());
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        String sideQrData = isValidQRCode(result.getText());
                        if (sideQrData == null) {
                            inValidQrCode();
                        } else {
                            Intent intent = new Intent("from_scan");
                            intent.putExtra("broadcastdata",sideQrData);
                            intent.putExtra("broadcastSide",sideAOrB);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            finish();
                        }
                    }, 500);
                }
            });

        });
        scanner_view.setOnClickListener(view -> {
            codeScanner.startPreview();
        });
    }

    private void initialisation() {
        scanner_view = findViewById(R.id.scanner_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private String isValidQRCode(String qrCodeText) {
        if (qrCodeText == null)
            return null;

        if (TextUtils.isEmpty(qrCodeText))
            return null;

        String[] results = qrCodeText.split("http://");
        if (results.length != 2)
            return null;

        if (results[1].trim().length() == 7) {
            return results[1].trim();
        }
        return null;
    }

    public void inValidQrCode() {
        AlertDialog.Builder inValidQrCodeAlert = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext()));
        inValidQrCodeAlert.setTitle("Invalid QR code");

        inValidQrCodeAlert.setMessage("Need valid Qr code to proceed, Please scan the valid Qr code.")
                .setCancelable(false)
                .setPositiveButton("Okay", (dialog, id) -> {
                    codeScanner.startPreview();
                    dialog.cancel();
                });
        AlertDialog alert = inValidQrCodeAlert.create();
        alert.show();
    }


}