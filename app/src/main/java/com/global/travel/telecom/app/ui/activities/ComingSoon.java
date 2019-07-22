package com.global.travel.telecom.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.global.travel.telecom.app.R;

public class ComingSoon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
    }

    public void backToDashboardButton(View view) {
        finish();
    }
    public void notificationButton(View view) {
        Intent i = new Intent(this, Notification.class);
        startActivity(i);
    }
}
