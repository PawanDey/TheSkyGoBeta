package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.model.SetDataInDashboard;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Dashboard extends BaseActivity {
    private double logitude;
    private double latitude;
    String token, adress5;
    String getCurrentBalance = "";
    TextView currentLocation, validity, balanceShowInDashboard;
    boolean doubleBackToExitPressedOnce;
    Context context = this;
    public ImageView setImageOnHotspot, skygoDialerLogo;
    AuthenticationPresenter authenticationPresenter;
    UserDetails userDetails;


    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userDetails = new UserDetails(this);
        Locale locale = new Locale(userDetails.getLanguageSelect());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        authenticationPresenter = new AuthenticationPresenter(this);
        skygoDialerLogo = findViewById(R.id.skyGoDialer);
        setImageOnHotspot = findViewById(R.id.button25);
        balanceShowInDashboard = findViewById(R.id.balanceShowInDashboard);
        SelectLoginImage();
        token = userDetails.getTokenID();
        try {
            authenticationPresenter.GetSubscriber(userDetails.getTokenID());
        } catch (Exception e) {
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
            currentLocation = findViewById(R.id.hereIsLocation);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            assert location != null;
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
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        if ("GetSubscriber".equals(method2)) {
            if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                Toast.makeText(Dashboard.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                showToast(errorMessage);
            }
        }
    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "SetDataInDashboard": {
                SetDataInDashboard obj = (SetDataInDashboard) response;
                userDetails.setVoipUserName(obj.getVoipName());
                userDetails.setVoipCredentailuserName(obj.getVoipUsername());
                userDetails.setVoipCredentailPassword(obj.getVoipPassword());
                userDetails.setVoipCustomerID(obj.getCustomerId());
                userDetails.setVoipSubcriberID(obj.getSubscriberId());
                userDetails.setUserId(obj.getCustomerReference());
                getCurrentBalance = "<get-customer-balance version=\"1\">\n" +
                        "<authentication>\n" +
                        "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                        "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                        "</authentication>\n" +
                        "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                        "</get-customer-balance>";
                authenticationPresenter.VoIPAPICall(getCurrentBalance, "getCurrentBalance");
                break;
            }
            case "GetSubscriber": {
                GetSIMStatus obj = (GetSIMStatus) response;
                userDetails = new UserDetails(this);
                TextView SimStatus = findViewById(R.id.activateSim);
                TextView SIMSerialNumber = findViewById(R.id.hideActivationtext);

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
                userDetails = new UserDetails(this);
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
                break;
            }
            case "CurrentBalance": {
                try {
                    CurrentBalance currentBalance = (CurrentBalance) response;
                    String bal = currentBalance.getGetCustomerBalanceResponse().getClearedBalance().getContent();
                    balanceShowInDashboard.setText("Balance: $" + bal.substring(0, bal.length() - 2));
                } catch (Exception e) {
                    showToast("Current Balanced Not show");
                }
                break;
            }
        }
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
            List<Address> addresses = geocoder.getFromLocation(latitude, logitude, 1);
            adress5 = addresses.get(0).getAddressLine(0);
            currentLocation.setText(adress5);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void hotspotButton(View view) {
        try {
            Hotspot hotspot = new Hotspot();
            hotspot.hotspotFxn(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SelectLoginImage() {
        userDetails = new UserDetails(this);
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
        switch (languageCode) {
            case "en":

                break;
            case "zh":
                try {
                    skygoDialerLogo.setImageResource(R.drawable.skygodialer_che);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "ja":
                try {
                    skygoDialerLogo.setImageResource(R.drawable.skygodialer_jep);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            case "ko":
                try {
                    skygoDialerLogo.setImageResource(R.drawable.skygodialer_kor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "es":
                try {
                    skygoDialerLogo.setImageResource(R.drawable.skygo_es);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                showToast(" no logo select ");
                break;
        }
    }

    public void googleMapOpen(View view) {
        //here is google map api call
        Intent intent = new Intent(Dashboard.this, GoogleMapActivity.class);
        intent.putExtra("logitude", logitude);
        intent.putExtra("latitude", latitude);
        startActivity(intent);
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

    public void clickOnESimActivation(View view) {
        setURL("https://www.skygo.celitech.app");
    }

    public void clickOnYelp(View view) {
        setURL("https://www.yelp.com");
    }

    public void clickOnGolfnow(View view) {
        setURL("https://www.golfnow.com");
    }

    private void setURL(String getURL) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
        startActivity(browserIntent);
    }

    public void allTypeOfBookingSoonFunction(View view) {
//        Intent i = new Intent(Dashboard.this, VoipLogin.class);
//        startActivity(i);
    }

    private void onLocationCahange(Location location) {
        logitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    public void btnExtendMSISDNClick(View view) {
        Intent intent = new Intent(Dashboard.this, Recharge.class);
        startActivity(intent);
    }

    public void btnActivateSIMClick(View view) {
        userDetails = new UserDetails(this);
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
//        Toast.makeText(this, R.string.textComingSoon, Toast.LENGTH_SHORT).show();
    }
}