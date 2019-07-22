package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    ImageView image;
    AuthenticationPresenter authenticationPresenter;
    String onCallTokenID;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        final UserDetails userDetails = new UserDetails(SplashScreen.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                authenticationPresenter.loginUser( userDetails.getUserName() ,LoginRequestTypeId.GOOGLE , "");

                UserDetails userDetails = new UserDetails(SplashScreen.this);

                Locale locale = new Locale(userDetails.getLanguageSelect());
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());

                if (userDetails.getTokenID() != "") //&& userDetails.getTokenID()==onCallTokenID
                {
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
        macAddress();

    }

//    public void onSuccess(String method, Object response) {
//        switch (method){
//            case "loginuser" :{
//                LoginResponse loginResponse = (LoginResponse) response;
//                onCallTokenID = loginResponse.getTokenID();
//            }
//        }
//    }


    private void moveAnimation() {
        Animation img = new TranslateAnimation(Animation.ABSOLUTE, 150, Animation.ABSOLUTE, Animation.ABSOLUTE);
        img.setDuration(2800);
        img.setFillAfter(true);
        image.startAnimation(img);
    }

    private void macAddress() {
        UserDetails userDetails = new UserDetails(this);

        //IMEI datails
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (!(ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        IMEI_Number_Holder = telephonyManager.getDeviceId();
        userDetails.setMacAddress(IMEI_Number_Holder);
    }

}