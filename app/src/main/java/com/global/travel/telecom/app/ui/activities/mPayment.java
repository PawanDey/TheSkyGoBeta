package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.aware.WifiAwareManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddFundsResponse;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.UpdateFundReq;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.lang.reflect.Method;
import java.math.BigDecimal;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class mPayment extends BaseActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    Button payPalPaymentButton;
    private mPayment mPayment;
    TextView m_response;
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AenMZYOK_JJk-FKV7trJDtyyUSOiZJgvSc06FTf5ZH46qnW1xD16LzcJHThGeaSSkB-KMp5qbYYDVpRd";   //sandbox
    //    String m_paypalClientId = " ";     // live
    Intent m_service;
    int m_paypalRequestCode = 999;
    TextView SimNumber;
    TextView Amount;
    TextView CartAmount;
    NewActivationRequest newActivationRequest;
    TextView Convenience;
    TextView AmountPayabale;
    //    int convence_fee = 0;
    TextView NumberOfDays;
    TextView ActivationDate;
    AuthenticationPresenter authenticationPresenter;
    AddFundsApp addFundsApp;
    UpdateFundReq updateFundReq = new UpdateFundReq();
    Random random = new Random();
    String updateFundID = "";
    String sessionTxnID = "";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private SharedPreferences permissionStatus;

    @Override
    protected int getLayout() {
        return R.layout.activity_m_payment;
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "activateSim": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
//                    showToast("Please Login again");
                    Toast.makeText(mPayment.this, R.string.textPleaseLoginagain, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    //showToast(errorMessage);
                }
            }
        }
    }

    private boolean sentToSettings = false;
    String IPaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_payment);

        //location and permission
        launchActivity();
        check();
        checkPlayServices();
        NetwordDetect();
        permissionStatus = PreferenceManager
                .getDefaultSharedPreferences(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);
        //location and permission ends here

        addFundsApp = new AddFundsApp();
        Bundle extras = getIntent().getExtras();
        authenticationPresenter = new AuthenticationPresenter(this);
        SimNumber = findViewById(R.id.simNumber);
        Amount = findViewById(R.id.textView7);
        CartAmount = findViewById(R.id.textView9);
        Convenience = findViewById(R.id.textView11);
        AmountPayabale = findViewById(R.id.editAmount1);
        NumberOfDays = findViewById(R.id.textView17);
        ActivationDate = findViewById(R.id.activationFromDate);
        UserDetails userDetails = new UserDetails(this);
        if (userDetails.getRechargeStatus() == 1) {
            SimNumber.setText(extras.getString("SerialNumber"));
            Amount.setText("$ " + extras.getString("AmountCharged"));
            CartAmount.setText("$ " + extras.getString("AmountCharged"));
            AmountPayabale.setText("$ " + extras.getString("AmountCharged"));
            NumberOfDays.setText(extras.getString("NumberOfDays"));
            ActivationDate.setText(extras.getString("RequestedForDtTm"));
        } else if (userDetails.getRechargeStatus() == 0) {
            SimNumber.setText(extras.getString("MSISDN"));
            Amount.setText("$ " + extras.getString("AmountChargedR"));
            CartAmount.setText("$ " + extras.getString("AmountChargedR"));
            AmountPayabale.setText("$ " + extras.getString("AmountChargedR"));
            NumberOfDays.setText(extras.getString("NumberOfDaysR"));
            ActivationDate.setText(extras.getString("RequestedForDtTmR"));
        }
//        Convenience.setText(convence_fee);


        payPalPaymentButton = findViewById(R.id.payPalPaymentActivityButton);
        m_response = findViewById(R.id.text1);
        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
                .clientId(m_paypalClientId).rememberUser(false);
        payPalPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPaymentOnclick(v);
            }
        });


        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
        startService(m_service);

    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "activateSim": {
                UserDetails userDetails = new UserDetails(this);
//                Date d = new Date();
//                CharSequence DateToday = DateFormat.format("MMMM d, yyyy ", d.getTime());
                userDetails.setRechargeStatus(0);
                Intent i = new Intent(mPayment.this, PaymentSucessfull.class);
                startActivity(i);
                finish();
                break;
            }

            case "ExtensionRequest": {
                Intent paymentsuccess = new Intent(mPayment.this, PaymentSucessfull.class);
                startActivity(paymentsuccess);
                finish();
                break;
            }

            case "UpdateFunds": {
                AddFundsResponse addFundsResponse = (AddFundsResponse) response;
                Bundle extras = getIntent().getExtras();
                UserDetails userDetails = new UserDetails(this);
                if (userDetails.getRechargeStatus() == 1) {
                    newActivationRequest = new NewActivationRequest();
                    newActivationRequest.setNumberOfDays(extras.getString("NumberOfDays"));
                    newActivationRequest.setSerialNumber(extras.getString("SerialNumber"));
                    newActivationRequest.setAmountCharged(extras.getString("AmountCharged"));
                    newActivationRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
                    newActivationRequest.setToken(userDetails.getTokenID());
                    newActivationRequest.setRefNo(extras.getString("RefNo"));
                    newActivationRequest.setRequestedDevice("Mobile");
                    newActivationRequest.setRequestedIP(IPaddress);
                    newActivationRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
                    try {
                        authenticationPresenter.activateSim(newActivationRequest);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                    break;
                } else if (userDetails.getRechargeStatus() == 0) {
                    //Extension Request api
                    NewExtensionRequest newExtensionRequest = new NewExtensionRequest();
                    newExtensionRequest.setNumberOfDays(extras.getString("NumberOfDaysR"));
                    newExtensionRequest.setMSISDN(extras.getString("MSISDN"));
                    newExtensionRequest.setAmountCharged(extras.getString("AmountChargedR"));
                    newExtensionRequest.setRequestedForDtTm(extras.getString("RequestedForDtTmR"));
                    newExtensionRequest.setToken(userDetails.getTokenID());
                    newExtensionRequest.setRefNo(extras.getString("RefNoR"));
                    newExtensionRequest.setRequestedDevice(extras.getString("RequestedDeviceR"));
                    newExtensionRequest.setRequestedIP(extras.getString("RequestedIPR"));
                    newExtensionRequest.setRequestedOS(extras.getString("RequestedOSR"));
                    try {
                        authenticationPresenter.extensionRequest(newExtensionRequest);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                    break;
                }
                break;
            }

            case "AddFundsViaAPP": {
                try {
                    AddFundsResponse addFundsResponse = (AddFundsResponse) response;
                    Bundle extras = getIntent().getExtras();
                    updateFundID = addFundsResponse.getRequestId();
                    PayPalPayment payment;
                    try {
                        payment = new PayPalPayment(new BigDecimal(extras.getString("AmountCharged")), "USD", "SkyGo",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                    } catch (Exception e) {
                        payment = new PayPalPayment(new BigDecimal(extras.getString("AmountChargedR")), "USD", "SkyGo",
                                PayPalPayment.PAYMENT_INTENT_SALE);
                    }
                    Intent intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, m_paypalRequestCode);
                } catch (Exception e) {
                    showToast(e.toString());
                }
                break;

            }
        }
    }


    void PayPalPaymentOnclick(View view) {
        Bundle extras = getIntent().getExtras();
        double Deduction = 0;
        try {
            try {
                Deduction = Double.parseDouble(extras.getString("AmountCharged"));
            } catch (Exception e) {
                Deduction = Double.parseDouble(extras.getString("AmountChargedR"));
            }

            if ((Deduction > 0)) {

                UserDetails userDetails = new UserDetails(this);
                String paypalTxnNumber = String.format("%06d", random.nextInt(1000000));
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
                addFundsApp.setRequestedDevice("Mobile");
                addFundsApp.setRequestedIP(IPaddress);
                addFundsApp.setRequestedOS("Android|" + userDetails.getLanguageSelect());
                addFundsApp.setServiceCharge("0");
                addFundsApp.setTokenID(userDetails.getTokenID());
                addFundsApp.setTransactionReferenceID(sessionTxnID);
                addFundsApp.setTransactionType("0");
                try {
                    authenticationPresenter.AddFundsAPI(addFundsApp);
                } catch (Exception e) {
                    showToast(e.toString());
                }
            } else if (Deduction == 0) {
                newActivationRequest = new NewActivationRequest();
                UserDetails userDetails = new UserDetails(this);
                newActivationRequest.setNumberOfDays(extras.getString("NumberOfDays"));
                newActivationRequest.setSerialNumber(extras.getString("SerialNumber"));
                newActivationRequest.setAmountCharged(extras.getString("AmountCharged"));
                newActivationRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
                newActivationRequest.setToken(userDetails.getTokenID());
                newActivationRequest.setRefNo(extras.getString("RefNo"));
                newActivationRequest.setRequestedDevice(extras.getString("RequestedDevice"));
                newActivationRequest.setRequestedIP(IPaddress);
                newActivationRequest.setRequestedOS(extras.getString("RequestedOS"));
                try {
                    authenticationPresenter.activateSim(newActivationRequest);
                } catch (Exception e) {
                    showToast(e.toString());
                }

            }

        } catch (Exception e) {
            showToast(e.toString());
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == m_paypalRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation configuration = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (configuration != null) {
                    UserDetails userDetails = new UserDetails(this);
                    String state = configuration.getProofOfPayment().getState();
                    if (state.equals("approved")) {
                        m_response.setText("payment Approved");
                        updateFundReq.setId(updateFundID);
                        updateFundReq.setPaypalReferenceID(sessionTxnID); // send paypal id
                        updateFundReq.setPayPalResponse("payment Approved");
                        updateFundReq.setRequestStatusID("15");
                        updateFundReq.setTokenID(userDetails.getTokenID());
                        updateFundReq.setTransactionReferenceID(sessionTxnID);
                        try {
                            authenticationPresenter.UpdateFundsMethod(updateFundReq);
                        } catch (Exception e) {
                            showToast(e.toString());
                        }
                        //Update Funds API
                    } else {
                        m_response.setText("not apporved");
                        updateFundReq.setId(updateFundID);
                        updateFundReq.setPaypalReferenceID(sessionTxnID);
                        updateFundReq.setPayPalResponse("not apporved");
                        updateFundReq.setRequestStatusID("16");
                        updateFundReq.setTransactionReferenceID(sessionTxnID);
                        try {
                            authenticationPresenter.UpdateFundsMethod(updateFundReq);
                        } catch (Exception e) {
                            showToast(e.toString());
                        }
                    }
                } else {
                    m_response.setText("confirmation is null");
                    updateFundReq.setId(updateFundID);
                    updateFundReq.setPaypalReferenceID(sessionTxnID);
                    updateFundReq.setPayPalResponse("confirmation is null");
                    updateFundReq.setRequestStatusID("16");
                    updateFundReq.setTransactionReferenceID(sessionTxnID);
                    try {
                        authenticationPresenter.UpdateFundsMethod(updateFundReq);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                }


            } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(this, "Invalid ", Toast.LENGTH_SHORT).show();
        } else if (requestCode == Activity.RESULT_CANCELED)
            Toast.makeText(this, "Cancel ", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onFailure() {
//        showToast("Sorry! Something went wrong");
        Toast.makeText(mPayment.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
    }

    public void notificatioButton(View view) {
        Intent i = new Intent(mPayment.this, Notification.class);
        startActivity(i);
    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;
    Context context = this;

    public void hotspotButton(View view) {
        Hotspot hotspot=new Hotspot();
        hotspot.hotspotFxn(context);
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
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mPayment.this);
//            dialog.setMessage("Gps is not enabled");
            dialog.setMessage(R.string.textGPSisnotenabled);
//            dialog.setPositiveButton("Open Setting", new DialogInterface.OnClickListener()
            dialog.setPositiveButton(R.string.textOpensettings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mPayment.this.startActivity(myIntent);
                    //get gps
                }
            });
//            dialog.setNegativeButton("if you cancel your app will be closed", new DialogInterface.OnClickListener() {
            dialog.setNegativeButton(R.string.textifyoucancelyourappwillbeclosed, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

//                @Override
//                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
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
                        sentToSettings = true;
                        // Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        // Uri uri = Uri.fromParts("package", getPackageName(), null);
                        // intent.setData(uri);
                        //startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location/\n" +
                                "अनुदान कैमरा और स्थान पर अनुमतियों पर जाएं", Toast.LENGTH_SHORT).show();
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


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.

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

    /**
     * If connected get lat and long
     */
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

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        //  Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }

    public String getDeviceIMEI() {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            } else {
                deviceUniqueIdentifier = tm.getDeviceId();

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

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if (WIFI == true) {
            IPaddress = GetDeviceipWiFiData();

        }

        if (MOBILE == true) {

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

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

}

