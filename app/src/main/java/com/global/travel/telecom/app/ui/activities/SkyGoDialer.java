package com.global.travel.telecom.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.global.travel.telecom.app.R;


public class SkyGoDialer extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky_go_dialer);


    }
    public void backToDashboardButton(View view){
        finish();

    }

    public void notificatioButton(View view){
        Intent i= new Intent(this,Notification.class);
        startActivity(i);
    }
}
