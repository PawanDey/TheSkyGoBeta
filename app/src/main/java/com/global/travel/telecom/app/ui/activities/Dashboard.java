package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;


public class Dashboard extends BaseActivity {

    //for current location
    public static TextView currentLocation, Button;
    private LocationManager locationManager;
    private double logitude;
    private double latitude;
    String token;
    TextView validity;
    ImageView skygoDialerLogo;
    boolean doubleBackToExitPressedOnce;
    public BaseView baseView;
    Context context = this;
    String isHotspotEnabled;
    public ImageView setImageOnHotspot;
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
        UserDetails userDetails = new UserDetails(this);
        Locale locale = new Locale(userDetails.getLanguageSelect());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        authenticationPresenter = new AuthenticationPresenter(this);
        skygoDialerLogo = findViewById(R.id.skyGoDialer);
        setImageOnHotspot = findViewById(R.id.button25);
        SelectLoginImage();
        //UserDetails userDetails = new UserDetails(Dashboard.this);
        // userDetails.getTokenID()

        try {
            authenticationPresenter.GetSubscriber(userDetails.getTokenID());
        } catch (Exception e) {
//            showToast(" "+R.string.textPleaseCheckYourInternetConnection);
            Toast.makeText(Dashboard.this, R.string.textPleaseCheckYourInternetConnection, Toast.LENGTH_LONG).show();
        }
        TextView SimStatus = findViewById(R.id.txtProcessPendingStatus);
        SimStatus.setVisibility(View.INVISIBLE);
        LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
        LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);
        TextView MSISDN = findViewById(R.id.txtMSISDN);
        TextView validityLeft = findViewById(R.id.txtValidityLeft);
        validity = findViewById(R.id.validityDateOnDeshboard);
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
            Toast.makeText(Dashboard.this, R.string.textLocationPermissionNotEnabled, Toast.LENGTH_LONG).show();

        }
        try {
            if (!userDetails.getMSISDN().isEmpty() || !userDetails.getActivationDate().isEmpty()) {
                MSISDN.setText(userDetails.getMSISDN());
                validityLeft.setText(userDetails.getActivationDate());
                ActivationLayout.setVisibility(View.GONE);
                RecentExtensionLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
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
//                    showToast(" "+R.string.textSorrySomethingwentwrong);
                    Toast.makeText(Dashboard.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                } else {
                    showToast(errorMessage);
                    break;
                }
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
                GetSIMStatus obj = (GetSIMStatus) response;

                switch (obj.getmValue()) {
                    case "1": {
                        SimStatus.setText(R.string.textStatusPending);
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatus(0);

                        break;
                    }
                    case "2": {
                        SimStatus.setText(R.string.textStatusUnderProcessing);
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatus(0);
                        break;
                    }
                }

                break;

            }
            case "GetSubscriber2": {
                UserDetails userDetails = new UserDetails(this);
                GetSubscriberResponse obj = (GetSubscriberResponse) response;
                userDetails.setMSISDN(obj.getMSISDN());
                userDetails.setActivationDate(obj.getActDate());
                LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
                LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);
                TextView MSISDN = findViewById(R.id.txtMSISDN);
                TextView validityLeft = findViewById(R.id.txtValidityLeft);
                ActivationLayout.setVisibility(View.GONE);
                RecentExtensionLayout.setVisibility(View.VISIBLE);
                MSISDN.setText(obj.getMSISDN());
                validityLeft.setText(obj.getActDate());
//                validity.setText(obj.getGoodUntil());
                break;
            }
        }
    }


    //end location


    public void btnActivateSIMClick(View view) {
        UserDetails userDetails = new UserDetails(this);
        if (userDetails.getRechargeStatus() == 1) {
            Intent intent = new Intent(Dashboard.this, ActivateSim.class);
            intent.putExtra("Token", token);
            startActivity(intent);
        }
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

    public void allTypeOfBookingSoonFunction(View view) {

        Intent i = new Intent(Dashboard.this, ComingSoon.class);
        startActivity(i);

    }

    private void onLocationCahange(Location location) {
        logitude = location.getLatitude();
        latitude = location.getLatitude();
    }

    public void btnExtendMSISDNClick(View view) {
        Intent intent = new Intent(Dashboard.this, Recharge.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.textClickBACKagaintoExit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
            UserDetails userDetails = new UserDetails(this);

//            authenticationPresenter.TranslateAPI("en", userDetails.getLanguageSelect(), city + ", " + adress1 + ", " + country);
            currentLocation.setText(city + ", " + adress1 + ", " + country);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;

    public void hotspotButton(View view) {
        try {
            Hotspot hotspot = new Hotspot();
            hotspot.hotspotFxn(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void hotspotButton(View view) {
//        Context context = this;
////        WifiManager.LocalOnlyHotspotReservation mReservation;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            final Dialog dialog = new Dialog(context);
//            if (mReservation == null) {
//                dialog.setContentView(R.layout.dialog_hotspot);
//                dialog.show();
//                TextView text2 = dialog.findViewById(R.id.textOk);
//                try {
//                    text2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                                wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
//                                    @Override
//                                    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
//                                        super.onStarted(reservation);
//                                        mReservation = reservation;
//                                        Toast.makeText(Dashboard.this, "HotSpot Start", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    @Override
//                                    public void onStopped() {
//                                        super.onStopped();
//                                        Toast.makeText(Dashboard.this, "Stop", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    @Override
//                                    public void onFailed(int reason) {
//                                        super.onFailed(reason);
//                                        Toast.makeText(Dashboard.this, "Fail", Toast.LENGTH_LONG).show();
//                                    }
//                                }, new Handler());
//                                dialog.dismiss();
//                            } catch (Exception e) {
//                                Toast.makeText(Dashboard.this, "Already ruuning State", Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                                dialog.dismiss();
//                            }
//                        }
//
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                dialog.setContentView(R.layout.dialog_hotspotoff);
//                dialog.show();
//                TextView text2 = dialog.findViewById(R.id.textOk);
//                try {
//                    text2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            try {
//                                mReservation.close();
//                                dialog.dismiss();
//                                mReservation = null;
//
//                                Toast.makeText(Dashboard.this, "close", Toast.LENGTH_LONG).show();
//                                isHotspotEnabled = "afterOff";
//
//
//                            } catch (Exception e) {
//                                Toast.makeText(Dashboard.this, e.toString(), Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                                dialog.dismiss();
//                            }
//                        }
////                        else {
////                            Toast.makeText(Dashboard.this, "Already HotSpot On", Toast.LENGTH_LONG).show();
////                            dialog.dismiss();
////                        }
//
//                    });
//                } catch (Exception e) {
//                    dialog.dismiss();
//                    e.printStackTrace();
//
//                }
//            }
//        }
//    }


//    public void btnRefresh(View view) {
//        Intent refresh = new Intent(Dashboard.this, Dashboard.class);
//        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refresh);
//    }

    public void SelectLoginImage() {
        UserDetails userDetails = new UserDetails(this);
        try {
            Locale locale = new Locale(userDetails.getLanguageSelect());
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String languageCode = userDetails.getLanguageSelect();
        //eng
        if (languageCode.equals("en")) {

        }
        //che
        else if (languageCode.equals("zh")) {
            try {
                skygoDialerLogo.setImageResource(R.drawable.skygodialer_che);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (languageCode.equals("ja")) {
            try {
                skygoDialerLogo.setImageResource(R.drawable.skygodialer_jep);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        //kor
        else if (languageCode.equals("ko")) {
            try {
                skygoDialerLogo.setImageResource(R.drawable.skygodialer_kor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //es(spanish need to be added)
        else if (languageCode.equals("es")) {
            try {
                skygoDialerLogo.setImageResource(R.drawable.skygo_es);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast(" no logo select ");

        }
    }

    public static void setLocation(String result) {
        currentLocation.setText(result);
    }


    public void clickOnTicketMaster(View view) {
        setURL("https://www.ticketmaster.com");
    }

    public void clickOnLyft(View view) {
        setURL("https://www.lyft.com/rider");
    }

    public void clickOnGrubHub(View view) {
        setURL("https://www.grubhub.com");
    }

    public void clickOnExpedia(View view) {
        setURL("https://www.expedia.com");
    }

    public void clickOncurrencyexchange(View view) {
        setURL("https://www.iceplc.com");
    }

    public void clickOnBuyAccessories(View view) {
        setURL("https://www.google.com");
    }

    public void clickOnPlayStore(View view) {
        setURL("https://play.google.com/store/apps");
    }

    public void clickOnGolfnow(View view) {
        setURL("https://www.golfnow.com");
    }

    private void setURL(String getURL) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
        startActivity(browserIntent);
    }

}