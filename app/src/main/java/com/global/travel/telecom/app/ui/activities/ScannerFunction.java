package com.global.travel.telecom.app.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFunction extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    ZXingScannerView scannerView;
    private String ActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Bundle extras = getIntent().getExtras();
        ActivityName = extras.getString("ActivityName");

    }

    @Override
    public void handleResult(Result result) {
        if (ActivityName.equals("ActivateData")) {
            ActivateSimDataServer.edtSerialNumber.setText(result.getText());
        } else {
            ActivateSim.edtSerialNumber.setText(result.getText());
        }
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}