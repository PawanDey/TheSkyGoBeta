package com.gtt.app.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gtt.app.model.GetSIMStatus;
import com.gtt.app.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gtt.app.base.BaseActivity;
import com.gtt.app.model.GetSIMStatus;
import com.gtt.app.model.GetSubscriberResponse;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Dashboard extends BaseActivity {

    //for current location
    private TextView currentLocation;
    private LocationManager locationManager;
    private double logitude;
    private double latitude;
    String token;

    //end location
    AuthenticationPresenter authenticationPresenter;

    //end location


    @Override
    protected int getLayout() {


        return R.layout.activity_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        String TokenTest = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6ImFiYyIsIm5iZiI6MTU2MTE1MTIxOCwiZXhwIjoxNTYxMTg3MjE4LCJpYXQiOjE1NjExNTEyMTh9.xJjXj1PkAq8PtQDcGvgxKizcVIGJLYZ7nvn5SIzDwNI";
        authenticationPresenter = new AuthenticationPresenter(this);
        UserDetails userDetails = new UserDetails(Dashboard.this);
        // userDetails.getTokenID()
        try {
            authenticationPresenter.GetSubscriber(userDetails.getTokenID());
        }
        catch (Exception e)
        {
            showToast("Please Check Your Internet Connection");
        }
        TextView SimStatus = findViewById(R.id.txtProcessPendingStatus);
        SimStatus.setVisibility(View.INVISIBLE);
        LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
        LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);

        try {
            //for location
            currentLocation = findViewById(R.id.hereIsLocation);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationCahange(location);
            log_func(location);

        } catch (Exception e) {
            showToast("Location Permission Not Enabled");

        }


    }



    @Override
    public void onFailure() {

    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "GetSubscriber": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    showToast("Please Login again");
                    Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else
                    showToast(errorMessage);
            }
        }
    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "GetSubscriber": {
                UserDetails userDetails = new UserDetails(this);
                TextView SimStatus = findViewById(R.id.activateSim);
                TextView SIMSerialNumber = findViewById(R.id.hideActivationtext);
                GetSIMStatus obj = (GetSIMStatus)response;

                switch (obj.getmValue())
                {
                    case "1" :{SimStatus.setText("Status : Pending");
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText("For " +obj.getmSerialNumber());

                        break;}
                    case "2" : {SimStatus.setText("Status : Under Processing");
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText("For " +obj.getmSerialNumber());
                        break;}
                }

                break;

            }
            case "GetSubscriber2" : {
                UserDetails userDetails = new UserDetails(this);
                GetSubscriberResponse obj = (GetSubscriberResponse) response;
                userDetails.setMSISDN(obj.getMSISDN());
                LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
                LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);
                TextView MSISDN = findViewById(R.id.txtMSISDN);
                TextView validityLeft = findViewById(R.id.txtValidityLeft);
                ActivationLayout.setVisibility(View.GONE);
                RecentExtensionLayout.setVisibility(View.VISIBLE);
                MSISDN.setText(obj.getMSISDN());
                validityLeft.setText(obj.getGoodUntil());
                break;
            }
        }
    }


    //end location


    public void btnActivateSIMClick(View view) {
        UserDetails userDetails = new UserDetails(this);
       if (userDetails.getRechargeStatus()==1)
       { Intent intent = new Intent(Dashboard.this, ActivateSim.class);
           intent.putExtra("Token", token);
           startActivity(intent);}
    }

    public void notificationButton(View view) {
        Intent i = new Intent(Dashboard.this, Notification.class);
        startActivity(i);
    }

    public void skyGoDailer(View view) {
        Intent intent = new Intent(Dashboard.this, SkyGoDialer.class);
        intent.putExtra("Token", token);
        startActivity(intent);
    }

    public void gridLayoutFunction(View view) {

        Intent i = new Intent(Dashboard.this, ComingSoon.class);
        startActivity(i);

    }

    private void onLocationCahange(Location location) {
        logitude = location.getLatitude();
        latitude = location.getLatitude();
    }

    public  void btnExtendMSISDNClick(View view)
    {
        Intent intent = new Intent(Dashboard.this, Recharge.class);
        startActivity(intent);
    }

    private void log_func(Location location) {

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            String country = addresses.get(0).getCountryName();
            String pincode = addresses.get(0).getPostalCode();
            String city = addresses.get(0).getLocality();
            String adress1 = addresses.get(0).getAdminArea();
            String adress2 = addresses.get(0).getCountryCode();
//            String adress3 =addresses.get(0).getPhone();
            String adress4 = addresses.get(0).getSubLocality();
            String adress5 = addresses.get(0).getAddressLine(0);
            String adress6 = addresses.get(0).getPremises();
            currentLocation.setText(city + ", " + adress1 + ", " + adress2);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;

    public void hotspotButton(View view) {


        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

                    @Override
                    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                        super.onStarted(reservation);
                        mReservation = reservation;
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }

                    @Override
                    public void onFailed(int reason) {
                        super.onFailed(reason);
                    }
                }, new Handler());
            } catch (Exception errorInHot) {
                errorInHot.printStackTrace();
            }
        }
    }
}