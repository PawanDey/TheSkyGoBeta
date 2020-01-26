package com.global.travel.telecom.app.ui.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.service.UserDetails;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserDetails userDetails = new UserDetails(SplashScreen.this);
                Locale locale = new Locale(userDetails.getLanguageSelect());
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                if (!userDetails.getTokenID().equals("")) {
                    Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, LanguageSelect.class);
                    startActivity(intent);
                }
            }

        }, 3000);

        image = findViewById(R.id.imageView4);
        moveAnimation();

    }

    private void moveAnimation() {
        Animation img = new TranslateAnimation(Animation.ABSOLUTE, 150, Animation.ABSOLUTE, Animation.ABSOLUTE);
        img.setDuration(2800);
        img.setFillAfter(true);
        image.startAnimation(img);
    }

}