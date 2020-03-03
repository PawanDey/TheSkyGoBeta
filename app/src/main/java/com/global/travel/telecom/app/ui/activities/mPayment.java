package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.AddCustomerCreditModel;
import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddFundsResponse;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.UpdateFundReq;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.widget.Toast.LENGTH_LONG;
import static com.global.travel.telecom.app.service.APIClient.BACKEND_URL;

public class mPayment extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    Button payPalPaymentButton, payButton;
    TextView Convenience, AmountPayabale, NumberOfDays, ActivationDate, SimNumber, CartAmount, m_response, misidn_serial_voip_text, activationDetails,fromDate;
    RelativeLayout proceedRelativeLayout, payRelativeLayout;
    UserDetails userDetails;
    Bundle extras;

    AuthenticationPresenter authenticationPresenter;
    AddFundsApp addFundsApp;
    NewActivationRequest newActivationRequest;
    UpdateFundReq updateFundReq = new UpdateFundReq();
    Random random = new Random();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SharedPreferences permissionStatus;

    String updateFundID = "";
    String sessionTxnID = "";
    final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    double currentLatitude, currentLongitude;
    double Deduction = 0;

    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String paypalTxnNumber = String.format("%09d", random.nextInt(1000000000));
    String IPaddress;
    Context context = this;
//    String stripePublishableKey = "pk_test_txOKeTftLeseIaribQBfChbQ00y9ehlYJR";
    String stripePublishableKey = "pk_live_fWzHCP8XcqaNvCiN2bJAyC0S00kWwPRyOP";

    Boolean updateFundCheck = true;
    String AppPaymentType = "";

    private OkHttpClient httpClient = new OkHttpClient();
    private Stripe stripe;

    @Override
    protected int getLayout() {
        return R.layout.activity_m_payment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_payment);
        permissionStatus = PreferenceManager.getDefaultSharedPreferences(this);
        extras = getIntent().getExtras();
        launchActivity();
        check();
        checkPlayServices();
        NetwordDetect();
        StripeOnPageLoad();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 1000).setFastestInterval(1000);

        userDetails = new UserDetails(this);
        addFundsApp = new AddFundsApp();
        authenticationPresenter = new AuthenticationPresenter(this);
        newActivationRequest = new NewActivationRequest();

        activationDetails = findViewById(R.id.activationDetails);
        SimNumber = findViewById(R.id.simNumber);
        CartAmount = findViewById(R.id.textView9);
        Convenience = findViewById(R.id.textView11);
        AmountPayabale = findViewById(R.id.editAmount1);
        NumberOfDays = findViewById(R.id.textView17);
        ActivationDate = findViewById(R.id.activationFromDate);
        payPalPaymentButton = findViewById(R.id.payPalPaymentActivityButton);
        m_response = findViewById(R.id.text1);
        misidn_serial_voip_text = findViewById(R.id.misidn_serial_voip_text);
        proceedRelativeLayout = findViewById(R.id.proceedRelativeLayout);
        payRelativeLayout = findViewById(R.id.payRelativeLayout);
        fromDate =findViewById(R.id.fromDate);

        AppPaymentType = extras.getString("AppPaymentType");   //1 =activation    2= extension     3= voip

        CartAmount.setText("$ " + extras.getString("AmountCharged"));
        AmountPayabale.setText("$ " + extras.getString("AmountCharged"));
        NumberOfDays.setText(extras.getString("NumberOfDays"));
        ActivationDate.setText(extras.getString("RequestedForDtTm"));

        assert AppPaymentType != null;
        switch (AppPaymentType) {
            case "1":
                misidn_serial_voip_text.setText(R.string.textSIMSerialNo);
                SimNumber.setText(extras.getString("Number"));
                break;
            case "2":
                misidn_serial_voip_text.setText(R.string.textMobileNumber);
                SimNumber.setText(extras.getString("Number"));
                fromDate.setText("Extension From Date - ");
                break;
            case "3":
                misidn_serial_voip_text.setText("Voip Plan");
                SimNumber.setText(extras.getString("Number"));
                activationDetails.setText("Voip Plan Detail");
                break;
            default:
                showToast("Error to select payment type i.e.(1,2,3)");
                break;

        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "ActivationExtensionRequest": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    Toast.makeText(mPayment.this, R.string.textPleaseLoginagain, LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;
            }
            case "UpdateFunds": {
                Toast.makeText(this, "Please try again", LENGTH_LONG).show();
                break;
            }
            case "AddFundsViaAPP": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    Toast.makeText(mPayment.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast(errorMessage);
                }
                break;
            }
            case "AddCustomerCredit":
            case "ApplyPromotion": {
                showToast(errorMessage);
                break;
            }
        }
    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "activateSim": {
                userDetails.setRechargeStatus(0);
                Intent i = new Intent(mPayment.this, PaymentSucessfull.class);
                i.putExtra("screenType", "1");
                startActivity(i);
                finish();
                break;
            }

            case "ExtensionRequest": {
                Intent paymentsuccess = new Intent(mPayment.this, PaymentSucessfull.class);
                paymentsuccess.putExtra("screenType", "2");
                startActivity(paymentsuccess);
                finish();
                break;
            }

            case "ApplyPromotion": {
                Intent intent = new Intent(mPayment.this, PaymentSucessfull.class);
                intent.putExtra("screenType", "3");
                startActivity(intent);
                finish();
                break;
            }

            case "AddCustomerCredit": {
                AddCustomerCreditModel addCustomerCredit = (AddCustomerCreditModel) response;
                String Balance = addCustomerCredit.getApplyCustomerCreditResponse().getBalance().getContent().trim();
                Intent intent = new Intent(mPayment.this, PaymentSucessfull.class);
                intent.putExtra("msg", "Your TopUp of $" + extras.getString("AmountCharged").trim() + " is successful and your current balance is $" + Balance.substring(0, Balance.length() - 2));
                intent.putExtra("screenType", "4");
                startActivity(intent);
                finish();
                break;
            }

            case "UpdateFunds": {
                if (updateFundCheck) {
                    break;
                }
                switch (AppPaymentType) {
                    case "1":
                        activateSim();
                        break;
                    case "2":
                        extensionRequest();
                        break;
                    case "3":
                        VoipPlanRecharge();
                        break;
                }
                break;
            }

            case "AddFundsViaAPP": {
                AddFundsResponse addFundsResponse = (AddFundsResponse) response;
                updateFundID = addFundsResponse.getRequestId();
                payRelativeLayout.setVisibility(View.VISIBLE);
                proceedRelativeLayout.setVisibility(View.GONE);
                break;
            }
        }
    }


    public void PayPalPaymentOnclick(View view) {
        try {
            assert extras != null;
            Deduction = Double.parseDouble(Objects.requireNonNull(extras.getString("AmountCharged")));
            sessionTxnID = userDetails.getTxnSeriesPrefix() + paypalTxnNumber;


            if (Deduction == 0) {
                switch (AppPaymentType) {
                    case "1":
                        activateSim();
                        break;
                    case "2":
                        extensionRequest();
                        break;
                    case "3":
                        VoipPlanRecharge();
                        break;
                }
            } else {
                addFundsApp.setAmountCharged(String.valueOf(Deduction));
                addFundsApp.setDealerID("0");
                addFundsApp.setIMEI(getDeviceIMEI());
                addFundsApp.setLatitude(String.valueOf(currentLatitude));
                addFundsApp.setLongitude(String.valueOf(currentLongitude));
                addFundsApp.setMacID("");
                addFundsApp.setPayPalRefNo("");
                addFundsApp.setPaymentMode("2");
                addFundsApp.setRemarks("Stripe Topup | Payment From Android APP");
                addFundsApp.setRequestedDevice(getDeviceName());
                addFundsApp.setRequestedIP(IPaddress);
                addFundsApp.setRequestedOS("Android | " + userDetails.getLanguageSelect());
                addFundsApp.setServiceCharge("0");
                addFundsApp.setTokenID(userDetails.getTokenID());
                addFundsApp.setTransactionReferenceID(sessionTxnID);
                addFundsApp.setTransactionType("0");
                authenticationPresenter.AddFundsAPI(addFundsApp);
            }
        } catch (Exception e) {
            showToast(e.toString());
        }

    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
        }
    }

    public void notificationButton(View view) {
        Intent i = new Intent(mPayment.this, Notification.class);
        startActivity(i);
    }

    public void hotspotButton(View view) {
        try {
            Hotspot hotspot = new Hotspot();
            hotspot.hotspotFxn(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToDashboardButton(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void check() {
        LocationManager lm = (LocationManager) mPayment.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(mPayment.this);
            dialog.setMessage(getResources().getString(R.string.textGPSisnotenabled));
            dialog.setPositiveButton(getResources().getString(R.string.textOpensettings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mPayment.this.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getResources().getString(R.string.textifyoucancelyourappwillbeclosed), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    private void launchActivity() {
        if (ActivityCompat.checkSelfPermission(mPayment.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mPayment.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mPayment.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(mPayment.this, permissionsRequired[1])
            ) {                //Show Information about why you need the permission
                ActivityCompat.requestPermissions(mPayment.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();    //here is bug (null point exception)
            editor.putBoolean(permissionsRequired[0], true);
            editor.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        launchActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
               } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
               Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @SuppressLint("HardwareIds")
    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            } else {
                try {
                    deviceUniqueIdentifier = tm.getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
                deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceUniqueIdentifier;
    }

    private void NetwordDetect() {
        boolean WIFI = false;
        boolean MOBILE = false;
        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert CM != null;
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    WIFI = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    MOBILE = true;
        }
        if (WIFI) {
            IPaddress = GetDeviceipWiFiData();
        }
        if (MOBILE) {
            IPaddress = GetDeviceipMobileData();
        }
    }

    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wm != null;
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public String getDeviceName() {
        String MANUFACTURER = Build.MANUFACTURER;
        String model = Build.MODEL;
        return MANUFACTURER + "__" + model;
    }

    private void activateSim() {
        newActivationRequest = new NewActivationRequest();
        newActivationRequest.setNumberOfDays(extras.getString("NumberOfDays"));
        newActivationRequest.setSerialNumber(extras.getString("Number"));
        newActivationRequest.setAmountCharged(extras.getString("AmountCharged"));
        newActivationRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
        newActivationRequest.setToken(userDetails.getTokenID());
        newActivationRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
        newActivationRequest.setRequestedDevice(getDeviceName());
        newActivationRequest.setRequestedIP(IPaddress);
        newActivationRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
        authenticationPresenter.activateSim(newActivationRequest);
    }

    private void extensionRequest() {
        NewExtensionRequest newExtensionRequest = new NewExtensionRequest();
        assert extras != null;
        newExtensionRequest.setNumberOfDays(extras.getString("NumberOfDays"));
        newExtensionRequest.setMSISDN(extras.getString("Number"));
        newExtensionRequest.setAmountCharged(extras.getString("AmountCharged"));
        newExtensionRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
        newExtensionRequest.setToken(userDetails.getTokenID());
        newExtensionRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
        newExtensionRequest.setRequestedDevice(getDeviceName());
        newExtensionRequest.setRequestedIP(IPaddress);
        newExtensionRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
        authenticationPresenter.extensionRequest(newExtensionRequest);

    }

    private void VoipPlanRecharge() {

        switch (Objects.requireNonNull(extras.getString("type"))) {
            case "AddPlan": {
                String xmlApplyPromotion = "<apply-promotion version=\"1\">\n" +
                        "<authentication>\n" +
                        "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                        "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                        "</authentication>\n" +
                        "<subscriberid>" + userDetails.getVoipSubcriberID().trim() + "</subscriberid>\n" +
                        "<promotion>" + extras.getString("MonikerValue") + "</promotion> " +
                        "<start-time>" + extras.getString("RequestedForDtTm") + "</start-time>" +
                        "<notify-on-depletion>no</notify-on-depletion>" +
                        "</apply-promotion>";
                authenticationPresenter.VoIPAPICall(xmlApplyPromotion, "ApplyPromotion");
                break;
            }
            case "AddBalance": {
                String xmlApplyPromotion = "<apply-customer-credit version=\"1\">\n" +
                        "<authentication>\n" +
                        "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                        "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                        "</authentication>\n" +
                        "<subscriberid>" + userDetails.getVoipSubcriberID().trim() + "</subscriberid>\n" +
                        "<amount>" + extras.getString("AmountCharged").trim() + "</amount>\n" +
                        "<narrative>Promotion</narrative>\n" +
                        "</apply-customer-credit>";
                authenticationPresenter.VoIPAPICall(xmlApplyPromotion, "AddCustomerCredit");
                break;
            }
        }
    }


    //stripe method
    private void StripeOnPageLoad() {
        CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        cardInputWidget.setShouldShowPostalCode(false);
        cardInputWidget.clear();

        onRetrievedKey(stripePublishableKey);
    }

    private void pay() {
        showProgressBar();
        CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            hideProgressBar();
            return;
        }
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                pay(result.id);
            }

            @Override
            public void onError(@NonNull Exception e) {
                showToast("Error in CreatePaymentMethod: " + e.getMessage());
                hideProgressBar();
            }
        });
    }

    private void pay(@Nullable String paymentMethodId) {
        final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        final String json;
        json = "{"
                + "\"useStripeSdk\":true,"
                + "\"paymentMethodId\":" + "\"" + paymentMethodId + "\","
                + "\"currency\":\"usd\","
                + "\"amount\":\"" + extras.getString("AmountCharged").replace(".", "") + "\","
                + "\"items\":["
                + "{\"id\":\"photo_subscription\"}"
                + "]"
                + "}";
        RequestBody body = RequestBody.create(json, mediaType);
        okhttp3.Request request = new okhttp3.Request.Builder().url(BACKEND_URL + "pay.php").post(body).build();
        httpClient.newCall(request).enqueue(new PayCallback(this, stripe));
    }

    private void displayAlert(@NonNull String title, @NonNull String message, boolean PaymentSuccess) {
        if (PaymentSuccess) {
            updateFundCheck = false;
            UpdateFundMethod(message, "payment success", "15");
        } else {
            runOnUiThread(() -> {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(message);
                new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                builder.setPositiveButton("Ok", null);
                builder.create()
                        .show();
            });
        }
    }

    private void onRetrievedKey(@NonNull String stripePublishableKey) {
        Context applicationContext = getApplicationContext();
        PaymentConfiguration.init(applicationContext, stripePublishableKey);
        stripe = new Stripe(applicationContext, stripePublishableKey);
        payButton = findViewById(R.id.payPalPaymentActivityButton);
        payButton.setOnClickListener((View view) -> {
            pay();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<mPayment> activityRef;
        @NonNull
        private final Stripe stripe;

        private PayCallback(@NonNull mPayment activity, @NonNull Stripe stripe) {
            this.activityRef = new WeakReference<>(activity);
            this.stripe = stripe;
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final mPayment activity = activityRef.get();
            hideProgressBar();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() -> Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final okhttp3.Response response)
                throws IOException {
            final mPayment activity = activityRef.get();

            if (activity == null) {
                showToast("PayCallback class Error: activity is null");
                hideProgressBar();
                return;
            }

            if (!response.isSuccessful()) {
                hideProgressBar();
                activity.runOnUiThread(() -> Toast.makeText(activity, " Not successfull: " + response.toString(), Toast.LENGTH_LONG).show());
            } else {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> responseMap = gson.fromJson(Objects.requireNonNull(response.body()).string(), type);
                String error = responseMap.get("error");
                String paymentIntentClientSecret = responseMap.get("clientSecret");
                String requiresAction = responseMap.get("requiresAction");

                if (error != null) {
                    hideProgressBar();
                    activity.displayAlert("Error", error, false);
                } else if (paymentIntentClientSecret != null) {
                    if ("true".equals(requiresAction)) {
                        hideProgressBar();
                        activity.runOnUiThread(() ->
                                stripe.authenticatePayment(activity, paymentIntentClientSecret));
                    } else {
                        activity.displayAlert("Payment succeeded", paymentIntentClientSecret, true);
                    }
                }

            }
        }
    }


    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<mPayment> activityRef;

        PaymentResultCallback(@NonNull mPayment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final mPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                updateFundCheck = false;
                UpdateFundMethod(gson.toJson(paymentIntent), "payment success", "15");
            } else {
                updateFundCheck = true;
                UpdateFundMethod(paymentIntent.getId(), "payment failed :" + Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(), "16");
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final mPayment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.runOnUiThread(() -> {
                activity.displayAlert("Error", e.toString(), false);
            });
        }
    }

    private void UpdateFundMethod(String PaypalReferanceID, String PaypalResponse, String RequestStatusID) {
        updateFundReq.setId(updateFundID);
        updateFundReq.setPaypalReferenceID(PaypalReferanceID);
        updateFundReq.setPayPalResponse(PaypalResponse);
        updateFundReq.setRequestStatusID(RequestStatusID);
        updateFundReq.setTokenID(userDetails.getTokenID());
        updateFundReq.setTransactionReferenceID(sessionTxnID);
        authenticationPresenter.UpdateFundsMethod(updateFundReq);
    }

}

