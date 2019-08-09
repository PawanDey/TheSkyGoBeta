package com.global.travel.telecom.app.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.global.travel.telecom.app.R;


public class SkyGoDialer extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView hotspotImage = findViewById(R.id.hotspotImage);
        setContentView(R.layout.activity_sky_go_dialer);
    }

    public void backToDashboardButton(View view) {
        finish();

    }

    public void notificatioButton(View view) {
        Intent i = new Intent(this, Notification.class);
        startActivity(i);
    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;
    Context context = this;

    public void hotspotButton(View view) {
        Hotspot hotspot = new Hotspot();
        hotspot.hotspotFxn(context);
    }
}
