package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class mPayment extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    Button payPalPaymentButton;
    TextView Convenience, AmountPayabale, NumberOfDays, ActivationDate, SimNumber, CartAmount, m_response, misidn_serial_voip_text, activationDetails;

    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AenMZYOK_JJk-FKV7trJDtyyUSOiZJgvSc06FTf5ZH46qnW1xD16LzcJHThGeaSSkB-KMp5qbYYDVpRd";   //the skygo production
    //    String m_paypalClientId = "AZhqNfrQvabHK5ohCMmSzh6Rt6o2krELyVYr1wxYRPe4IEkX-LsLa0i3lRSdUB2mR1apFsrZko5e6kng";    //enk production
//    String m_paypalClientId = "Acl-zy7SQufyYeOKKROTM37taRJcpe7ige_orlofjY_0YnZuxQ-PWL9vfdzxXWNFEOuQwA5WDPqL3Csw";   //Sky USA Inc
    Intent m_service;
//    int m_paypalRequestCode = 999;

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

    // latest code for braintRee
    final int REQUEST_CODE = 1;
    final String get_token = "https://www.sirrat.com/BraintreePayments/include/main.php"; // braintree/main.php
    final String send_payment_details = "https://www.sirrat.com/BraintreePayments/include/checkout.php"; //checkout.php
    String amount, token;
    HashMap<String, String> paramHash;
    Boolean updateFundCheck = true;
    String AppPaymentType = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_m_payment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_payment);

        launchActivity();
        check();
        checkPlayServices();
        NetwordDetect();

        permissionStatus = PreferenceManager.getDefaultSharedPreferences(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10 * 1000).setFastestInterval(1000);

        userDetails = new UserDetails(this);
        addFundsApp = new AddFundsApp();
        extras = getIntent().getExtras();
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

        assert extras != null;
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

        m_configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(m_paypalClientId).rememberUser(false);
        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        startService(m_service);

        payPalPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPaymentOnclick();
            }
        });

        showProgressBar();
        new HttpRequest().execute();

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
//                    showToast(" "+R.string.textSorrySomethingwentwrong);
                    Toast.makeText(mPayment.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast(errorMessage);
                }
                break;
            }
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
                startActivity(i);
                finish();
                break;
            }

            case "ExtensionRequest":

            case "ApplyPromotion": {
                Intent paymentsuccess = new Intent(mPayment.this, PaymentSucessfull.class);
                startActivity(paymentsuccess);
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
                try {

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
                        AddFundsResponse addFundsResponse = (AddFundsResponse) response;
                        updateFundID = addFundsResponse.getRequestId();
                        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
                    }

                } catch (Exception e) {
                    showToast(e.toString());
                }
                break;

            }
        }
    }


    void PayPalPaymentOnclick() {
        try {
            assert extras != null;
            Deduction = Double.parseDouble(Objects.requireNonNull(extras.getString("AmountCharged")));

            sessionTxnID = userDetails.getTxnSeriesPrefix() + paypalTxnNumber;
            addFundsApp.setAmountCharged(String.valueOf(Deduction));
            addFundsApp.setDealerID("0");
            addFundsApp.setIMEI(getDeviceIMEI());
            addFundsApp.setLatitude(String.valueOf(currentLatitude));
            addFundsApp.setLongitude(String.valueOf(currentLongitude));
            addFundsApp.setMacID("");
            addFundsApp.setPayPalRefNo("");
            addFundsApp.setPaymentMode("2");
            addFundsApp.setRemarks("Payment From Android APP");
            addFundsApp.setRequestedDevice(getDeviceName());
            addFundsApp.setRequestedIP(IPaddress);
            addFundsApp.setRequestedOS("Android | " + userDetails.getLanguageSelect());
            addFundsApp.setServiceCharge("0");
            addFundsApp.setTokenID(userDetails.getTokenID());
            addFundsApp.setTransactionReferenceID(sessionTxnID);
            addFundsApp.setTransactionType("0");
            authenticationPresenter.AddFundsAPI(addFundsApp);
        } catch (Exception e) {
            showToast(e.toString());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showProgressBar();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                assert result != null;
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                assert nonce != null;
                String stringNonce = nonce.getNonce();
                Log.d("mylog", "Result: " + stringNonce);
                // Send payment price with the nonce
                // use the result to update your UI and send the payment method nonce to your server
                try {
                    if (!AmountPayabale.getText().toString().isEmpty()) {
                        amount = AmountPayabale.getText().toString().replace("$", "").trim();
                        paramHash = new HashMap<>();
                        paramHash.put("amount", amount);
                        paramHash.put("nonce", stringNonce);
                        sendPaymentDetails();
                    } else {
                        Toast.makeText(mPayment.this, "amount+nonce error: else", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(mPayment.this, "amount+nonce error catch:" + e, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.d("mylog", "user canceled");
                showToast(getApplication().getString(R.string.textCancel));
                hideProgressBar();
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("mylog", "Error : " + error);
                showToast("Payment Error:" + getApplication().getString(R.string.textSorrySomethingwentwrong));
                hideProgressBar();
            }
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

    public void backToactivation(View view) {
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
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mPayment.this);
//            dialog.setMessage("Gps is not enabled");
            dialog.setMessage(getResources().getString(R.string.textGPSisnotenabled));
//            dialog.setPositiveButton("Open Setting", new DialogInterface.OnClickListener()
            dialog.setPositiveButton(getResources().getString(R.string.textOpensettings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mPayment.this.startActivity(myIntent);
                    //get gps
                }
            });
//            dialog.setNegativeButton("if you cancel your app will be closed", new DialogInterface.OnClickListener() {
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
            ) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mPayment.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant/अनुदान", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(mPayment.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel/रद्द करना", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(mPayment.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions./\n" +
                        "इस ऐप को कैमरा और स्थान अनुमतियों की आवश्यकता है।");
                builder.setPositiveButton("Grant/अनुदान", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        // Uri uri = Uri.fromParts("package", getPackageName(), null);
                        // intent.setData(uri);
                        //startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location/\n" +
                                "अनुदान कैमरा और स्थान पर अनुमतियों पर जाएं", LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel/रद्द करना", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
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
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //      Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
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

    private class HttpRequest extends AsyncTask {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get(get_token, new HttpResponseCallback() {
                @Override
                public void success(String responseBody) {
                    Log.d("mylog", responseBody);
                    token = responseBody;
                    hideProgressBar();
                }

                @Override
                public void failure(Exception exception) {
                    final Exception ex = exception;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mPayment.this, "Failed to get token: " + ex.toString(), Toast.LENGTH_LONG).show();
                            hideProgressBar();
                        }

                    });
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
        }

    }

    private void sendPaymentDetails() {

        RequestQueue queue = Volley.newRequestQueue(mPayment.this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_payment_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Braintree\\Result\\Successful")) {
                            Toast.makeText(mPayment.this, "Payment Successful", Toast.LENGTH_LONG).show();
                            try {
                                updateFundCheck = false;
                                String[] str = response.split(",");
                                List<String> al;
                                al = Arrays.asList(str);
                                for (String s : al) {
                                    System.out.println(s);
                                }
                                String[] str1 = al.get(0).split("=");
                                String tnxID = str1[1];
                                updateFundReq.setId(updateFundID);
                                updateFundReq.setPaypalReferenceID(tnxID); // send paypal id
                                updateFundReq.setPayPalResponse("Payment Approved");
                                updateFundReq.setRequestStatusID("15");
                                updateFundReq.setTokenID(userDetails.getTokenID());
                                updateFundReq.setTransactionReferenceID(sessionTxnID);
                                try {
                                    authenticationPresenter.UpdateFundsMethod(updateFundReq);
                                } catch (Exception e) {
                                    showToast(e.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (response.contains("Braintree\\Result\\Error")) {
                            Toast.makeText(mPayment.this, "Payment Failed", Toast.LENGTH_LONG).show();
                            try {
                                updateFundCheck = true;
                                String[] str = response.split("message=");
                                List<String> al;
                                al = Arrays.asList(str);
                                for (String s : al) {
                                    System.out.println(s);
                                }
                                String[] str1 = al.get(1).split(",");
                                updateFundReq.setId(updateFundID);
                                updateFundReq.setPaypalReferenceID("");   //not null ethe error
                                updateFundReq.setPayPalResponse("Not Approved|" + Arrays.toString(str1));
                                updateFundReq.setRequestStatusID("16");
                                updateFundReq.setTokenID(userDetails.getTokenID());
                                updateFundReq.setTransactionReferenceID(sessionTxnID);
                                try {
                                    authenticationPresenter.UpdateFundsMethod(updateFundReq);
                                } catch (Exception e) {
                                    showToast(e.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mylog", "Volley error : " + error.toString());
                updateFundCheck = true;
                Toast.makeText(mPayment.this, "Volley error Payment:" + error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                if (paramHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramHash.keySet()) {
                    params.put(key, paramHash.get(key));
                    Log.d("mylog", "Key : " + key + " Value : " + paramHash.get(key));
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
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
        String xmlApplyPromotion = "<apply-promotion version=\"1\">\n" +
                "<authentication>\n" +
                "<username>skygo.api</username>\n" +
                "<password>54321@123</password>\n" +
                "</authentication>\n" +
//                "<subscriberid>" + userDetails.getVoipSubcriberID().trim() + "</subscriberid>\n" +
                "<subscriberid>13799728</subscriberid>" +
                "<promotion>" + extras.getString("MonikerValue") + "</promotion> " +
                "<start-time>" + extras.getString("RequestedForDtTm") + "</start-time>" +
                "<notify-on-depletion>no</notify-on-depletion>" +
                "</apply-promotion>";
        authenticationPresenter.VoIPAPICall(xmlApplyPromotion, "ApplyPromotion");
    }

}

