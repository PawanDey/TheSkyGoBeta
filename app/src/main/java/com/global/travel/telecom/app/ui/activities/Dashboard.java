package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.model.SetDataInDashboard;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Dashboard extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    NavigationView navigationView;
    DrawerLayout drawer;
    private androidx.appcompat.app.AlertDialog progressDialog;
    public static String ServerName = "";  //{VoiceServer ,DataServer,VoIPServer} These are 3 type of serverName

    @Override
    protected int getLayout() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Toast.makeText(Dashboard.this, getResources().getString(R.string.textPleaseCheckYourInternetConnection), Toast.LENGTH_LONG).show();
        }

        try {
            authenticationPresenter.GetSubscriberDataServer(userDetails.getTokenIDDataServer());
        } catch (Exception e) {
            Toast.makeText(Dashboard.this, getResources().getString(R.string.textPleaseCheckYourInternetConnection), Toast.LENGTH_LONG).show();
        }

        try {
            getCurrentBalance = "<get-customer-balance version=\"1\">\n" +
                    "<authentication>\n" +
                    "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                    "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                    "</authentication>\n" +
                    "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                    "</get-customer-balance>";
            authenticationPresenter.VoIPAPICall(getCurrentBalance, "getCurrentBalance");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView SimStatus = findViewById(R.id.txtProcessPendingStatus);
        TextView SimStatusDataServer = findViewById(R.id.txtProcessPendingStatusDataServer);
        SimStatus.setVisibility(View.INVISIBLE);
        SimStatusDataServer.setVisibility(View.INVISIBLE);
        LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
        LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);
        TextView MSISDN = findViewById(R.id.txtMSISDN);
        TextView validityLeft = findViewById(R.id.txtValidityLeft);
        validity = findViewById(R.id.validityDateOnDeshboard);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

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
            log_func();

        } catch (Exception e) {
            Toast.makeText(Dashboard.this, getResources().getString(R.string.textLocationPermissionNotEnabled), Toast.LENGTH_LONG).show();
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (!userDetails.getMSISDN().isEmpty() || !userDetails.getActivationDate().isEmpty()) {
                    MSISDN.setText(userDetails.getMSISDN());
                    validityLeft.setText(userDetails.getActivationDate());
                    ActivationLayout.setVisibility(View.GONE);
                    RecentExtensionLayout.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textNOInternetConnection), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        if ("GetSubscriber".equals(method2)) {
            if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                Toast.makeText(Dashboard.this, getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
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

                break;
            }
            case "GetSubscriber": {
                GetSIMStatus obj = (GetSIMStatus) response;
                userDetails = new UserDetails(this);
                TextView SimStatus = findViewById(R.id.activateSim);
                TextView SIMSerialNumber = findViewById(R.id.hideActivationtext);

                switch (obj.getmValue()) {
                    case "1": {
                        SimStatus.setText(getResources().getString(R.string.textStatusPending));
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatus(0);

                        break;
                    }
                    case "2": {
                        SimStatus.setText(getResources().getString(R.string.textStatusUnderProcessing));
                        SimStatus.setVisibility(View.VISIBLE);
                        SIMSerialNumber.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatus(0);
                        break;
                    }
                    case "No Request Raised": {
                        LinearLayout ActivationLayout = findViewById(R.id.ActivateSimLayout);
                        LinearLayout RecentExtensionLayout = findViewById(R.id.RecentActivateOnMobile);
                        ActivationLayout.setVisibility(View.VISIBLE);
                        RecentExtensionLayout.setVisibility(View.GONE);
                        userDetails.setMSISDN("");
                        userDetails.setActivationDate("");
                        userDetails.setRechargeStatus(1);
                        break;
                    }
                }
                break;

            }
            case "GetSubscriberDataServer": {
                GetSIMStatus obj = (GetSIMStatus) response;
                userDetails = new UserDetails(this);
                TextView SimStatusDataServer = findViewById(R.id.activateSimDataServer);
                TextView SIMSerialNumberDataServer = findViewById(R.id.hideActivationtextDataServer);

                switch (obj.getmValue()) {
                    case "1": {
                        SimStatusDataServer.setText(getResources().getString(R.string.textStatusPending));
                        SimStatusDataServer.setVisibility(View.VISIBLE);
                        SIMSerialNumberDataServer.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatusDataServer(0);
                        break;
                    }
                    case "2": {
                        SimStatusDataServer.setText(getResources().getString(R.string.textStatusUnderProcessing));
                        SimStatusDataServer.setVisibility(View.VISIBLE);
                        SIMSerialNumberDataServer.setText(getResources().getString(R.string.textFor) + " " + obj.getmSerialNumber());
                        userDetails.setRechargeStatusDataServer(0);
                        break;
                    }
                    case "No Request Raised": {
                        LinearLayout ActivationLayoutDataServer = findViewById(R.id.ActivateSimLayoutDataServer);
                        LinearLayout RecentExtensionLayoutDataServer = findViewById(R.id.RecentActivateOnMobileDataServer);
//                        ActivationLayoutDataServer.setVisibility(View.VISIBLE);
                        RecentExtensionLayoutDataServer.setVisibility(View.GONE);
                        userDetails.setMSISDNDataServer("");
                        userDetails.setActivationDateDataServer("");
                        userDetails.setRechargeStatusDataServer(1);
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
                String[] gud = obj.getGoodUntil().trim().split("T");
                validityLeft.setText("Expiry Date: " + gud[0]);
                break;
            }
            case "GetSubscriber2DataServer": {
                userDetails = new UserDetails(this);
                GetSubscriberResponse obj = (GetSubscriberResponse) response;
                userDetails.setMSISDNDataServer(obj.getMSISDN());
                userDetails.setActivationDateDataServer(obj.getActDate());
                LinearLayout ActivationLayoutDataServer = findViewById(R.id.ActivateSimLayoutDataServer);
                LinearLayout RecentExtensionLayoutDataServer = findViewById(R.id.RecentActivateOnMobileDataServer);
                TextView MSISDNDataServer = findViewById(R.id.txtMSISDNDataServer);
                TextView validityLeftDataServer = findViewById(R.id.txtValidityLeftDataServer);
                ActivationLayoutDataServer.setVisibility(View.GONE);
                RecentExtensionLayoutDataServer.setVisibility(View.VISIBLE);
                MSISDNDataServer.setText(obj.getMSISDN());
                String[] gud = obj.getGoodUntil().trim().split("T");
                validityLeftDataServer.setText("Expiry Date: " + gud[0]);
                break;
            }
            case "CurrentBalance": {
                try {
                    CurrentBalance currentBalance = (CurrentBalance) response;
                    String bal = currentBalance.getGetCustomerBalanceResponse().getClearedBalance().getContent();
                    balanceShowInDashboard.setText(getResources().getString(R.string.textBalance) + ": $" + bal.substring(0, bal.length() - 2));
                } catch (Exception e) {
                    showToast("Current Balanced Not show");
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.textClickBACKagaintoExit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_profile: {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_Language_Change: {
                @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.dialog_language_change, null);
                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(view);
                progressDialog = mBuilder.create();
                Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                progressDialog.show();

                //ID
                TextView cancle = view.findViewById(R.id.langauge_change_Cancle);
                TextView OK = view.findViewById(R.id.langauge_change_OK);

                RadioButton SelectEnglish = view.findViewById(R.id.SelectEnglish);
                RadioButton SelectChinese = view.findViewById(R.id.SelectChinese);
                RadioButton SelectJapanese = view.findViewById(R.id.SelectJapanese);
                RadioButton SelectKorean = view.findViewById(R.id.SelectKorean);
                RadioButton SelectSPANISH = view.findViewById(R.id.SelectSPANISH);

                OK.setOnClickListener(v1 -> {

                    if (SelectEnglish.isChecked()) {
                        setLanguage("en");
                    } else if (SelectChinese.isChecked()) {
                        setLanguage("zh");
                    } else if (SelectJapanese.isChecked()) {
                        setLanguage("ja");
                    } else if (SelectKorean.isChecked()) {
                        setLanguage("ko");
                    } else if (SelectSPANISH.isChecked()) {
                        setLanguage("es");
                    } else {
                        showToast(getResources().getString(R.string.textSelectYourAppLanguage));
                    }
                });
                cancle.setOnClickListener(v12 -> progressDialog.dismiss());

                break;
            }
            case R.id.nav_transaction: {
                Intent intent = new Intent(this, TransactionDetails.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_InviteFriend: {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Get it for free at https://play.google.com/store/apps/details?id=com.global.travel.telecom.app");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            }
            case R.id.nav_Rate_Us: {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.nav_Feedback: {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "sumit.s@virtuzo.in", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "The SkyGo Feedback by User");
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hi SkyGo Team,");
                startActivity(Intent.createChooser(emailIntent, "The SkyGo Feedback"));
                break;
            }
            case R.id.nav_Customer_Service: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:919805198061"));
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(this, LoginActivity.class);
                userDetails.setMSISDN("");
                userDetails.removeTokenID();
                startActivity(intent);
                finish();
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hotspotButton(View view) {

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

    public void allTypeOfBookingSoonFunction(View view) {
//        Intent i = new Intent(Dashboard.this, VoipLogin.class);
//        startActivity(i);
    }

    public void btnExtendMSISDNClick(View view) {
        ServerName = "VoiceServer";
        Intent intent = new Intent(Dashboard.this, ExtensionSim.class);
        startActivity(intent);
    }

    public void btnActivateSIMClick(View view) {
        userDetails = new UserDetails(this);
        if (userDetails.getRechargeStatus() == 1) {
            ServerName = "VoiceServer";
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
        ServerName = "VoIPServer";
        Intent intent = new Intent(Dashboard.this, SkyGoDialer.class);
        intent.putExtra("Token", token);
        startActivity(intent);
//        Toast.makeText(this, getResources().getString(R.string.textComingSoon), Toast.LENGTH_LONG).show();
    }

    public void threeLines(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void btnActivateSIMClickDataServer(View view) {
        userDetails = new UserDetails(this);
        if (userDetails.getRechargeStatusDataServer() == 1) {
            ServerName = "DataServer";
            Intent intent = new Intent(Dashboard.this, ActivateSimDataServer.class);
            intent.putExtra("Token", userDetails.getTokenIDDataServer());
            startActivity(intent);
        }
    }

    public void btnExtendMSISDNClickDataServer(View view) {
        ServerName = "DataServer";
        Intent intent = new Intent(Dashboard.this, ExtensionSimDataServer.class);
        startActivity(intent);
    }

    private void setLanguage(String Locate) {
        Locale locale = new Locale(Locate);
        UserDetails userDetails = new UserDetails(this);
        userDetails.setLanguageSelect(locale.toString());
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        progressDialog.dismiss();
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void onLocationCahange(Location location) {
        logitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    private void setURL(String getURL) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
        startActivity(browserIntent);
    }

    private void SelectLoginImage() {
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
        }
    }

    private void log_func() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, logitude, 1);
            adress5 = addresses.get(0).getAddressLine(0);
            currentLocation.setText(adress5);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}