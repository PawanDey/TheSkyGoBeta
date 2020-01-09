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

import com.android.volley.AuthFailureError;
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
    TextView m_response;
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AenMZYOK_JJk-FKV7trJDtyyUSOiZJgvSc06FTf5ZH46qnW1xD16LzcJHThGeaSSkB-KMp5qbYYDVpRd";   //the skygo production
    //    String m_paypalClientId = "AZhqNfrQvabHK5ohCMmSzh6Rt6o2krELyVYr1wxYRPe4IEkX-LsLa0i3lRSdUB2mR1apFsrZko5e6kng";    //enk production
    //    String m_paypalClientId = "Acl-zy7SQufyYeOKKROTM37taRJcpe7ige_orlofjY_0YnZuxQ-PWL9vfdzxXWNFEOuQwA5WDPqL3Csw";   //Sky USA Inc
    Intent m_service;
    int m_paypalRequestCode = 999;
    TextView SimNumber;
    //    TextView Amount;
    TextView CartAmount;
    NewActivationRequest newActivationRequest;
    TextView Convenience;
    TextView AmountPayabale;
    TextView NumberOfDays;
    TextView ActivationDate;
    AuthenticationPresenter authenticationPresenter;
    AddFundsApp addFundsApp;
    UpdateFundReq updateFundReq = new UpdateFundReq();
    Random random = new Random();
    String updateFundID = "";
    String sessionTxnID = "";
    final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    double currentLatitude;
    double currentLongitude;
    static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SharedPreferences permissionStatus;
    String paypalTxnNumber = String.format("%09d", random.nextInt(1000000000));
    private boolean sentToSettings = false;
    String IPaddress;

    // latest code for braintRee
    final int REQUEST_CODE = 1;
    final String get_token = "https://www.sirrat.com/BraintreePayments/include/main.php"; // braintree/main.php
    final String send_payment_details = "https://www.sirrat.com/BraintreePayments/include/checkout.php"; //checkout.php
    String token;
    String amount;
    HashMap<String, String> paramHash;
    Boolean updateFundCheck = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_m_payment;
    }

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
                .setFastestInterval(1000);
        //location and permission ends here

        addFundsApp = new AddFundsApp();
        Bundle extras = getIntent().getExtras();
        authenticationPresenter = new AuthenticationPresenter(this);
        SimNumber = findViewById(R.id.simNumber);
//        Amount = findViewById(R.id.textView7);
        CartAmount = findViewById(R.id.textView9);
        Convenience = findViewById(R.id.textView11);
        AmountPayabale = findViewById(R.id.editAmount1);
        NumberOfDays = findViewById(R.id.textView17);
        ActivationDate = findViewById(R.id.activationFromDate);
        UserDetails userDetails = new UserDetails(this);
        if (userDetails.getRechargeStatus() == 1) {
            assert extras != null;
            SimNumber.setText(extras.getString("SerialNumber"));
//            Amount.setText("$ " + extras.getString("AmountCharged"));
            CartAmount.setText("$ " + extras.getString("AmountCharged"));
            AmountPayabale.setText("$ " + extras.getString("AmountCharged"));
            NumberOfDays.setText(extras.getString("NumberOfDays"));
            ActivationDate.setText(extras.getString("RequestedForDtTm"));
        } else if (userDetails.getRechargeStatus() == 0) {
            SimNumber.setText(extras.getString("MSISDN"));
//            Amount.setText("$ " + extras.getString("AmountChargedR"));
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
        showProgressBar();
        new HttpRequest().execute();

    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "activateSim": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
//                    showToast("Please Login again");
                    Toast.makeText(mPayment.this, R.string.textPleaseLoginagain, LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
            case "ExtensionRequest": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
//                    showToast("Please Login again");
                    Toast.makeText(mPayment.this, R.string.textPleaseLoginagain, LENGTH_LONG).show();
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
            case "UpdateFunds":{
                Toast.makeText(this,"Please try again", LENGTH_LONG).show();

            }
        }
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
                if (updateFundCheck == true) {

                    break;
                }
                UserDetails userDetails = new UserDetails(this);
                if (userDetails.getRechargeStatus() == 1) {
                    newActivationRequest = new NewActivationRequest();
                    assert extras != null;
                    newActivationRequest.setNumberOfDays(extras.getString("NumberOfDays"));
                    newActivationRequest.setSerialNumber(extras.getString("SerialNumber"));
                    newActivationRequest.setAmountCharged(extras.getString("AmountCharged"));
                    newActivationRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
                    newActivationRequest.setToken(userDetails.getTokenID());
                    newActivationRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
                    newActivationRequest.setRequestedDevice(getDeviceName());
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
                    assert extras != null;
                    newExtensionRequest.setNumberOfDays(extras.getString("NumberOfDaysR"));
                    newExtensionRequest.setMSISDN(extras.getString("MSISDN"));
                    newExtensionRequest.setAmountCharged(extras.getString("AmountChargedR"));
                    newExtensionRequest.setRequestedForDtTm(extras.getString("RequestedForDtTmR"));
                    newExtensionRequest.setToken(userDetails.getTokenID());
                    newExtensionRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
                    newExtensionRequest.setRequestedDevice(getDeviceName());
                    newExtensionRequest.setRequestedIP(IPaddress);
                    newExtensionRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
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
                    updateFundReq.setId(updateFundID);

                    //old paypal function is start form here
//                    PayPalPayment payment;
//                    try {
//                        assert extras != null;
//                        payment = new PayPalPayment(new BigDecimal(extras.getString("AmountCharged")), "USD", "The SkyGo",
//                                PayPalPayment.PAYMENT_INTENT_SALE);
//                    } catch (Exception e) {
//                        payment = new PayPalPayment(new BigDecimal(extras.getString("AmountChargedR")), "USD", "The SkyGo",
//                                PayPalPayment.PAYMENT_INTENT_SALE);
//                    }
//                    Intent intent = new Intent(this, PaymentActivity.class);
//                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
//                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//                    startActivityForResult(intent, m_paypalRequestCode);
                    //end here

                    DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                    startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);

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
                assert extras != null;
                Deduction = Double.parseDouble(Objects.requireNonNull(extras.getString("AmountCharged")));
            } catch (Exception e) {
                Deduction = Double.parseDouble(Objects.requireNonNull(extras.getString("AmountChargedR")));
            }
            @SuppressLint("DefaultLocale")
            UserDetails userDetails = new UserDetails(this);
            sessionTxnID = userDetails.getTxnSeriesPrefix() + paypalTxnNumber;

            if (userDetails.getRechargeStatus() == 1) {
                if ((Deduction > 0)) {
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
                    try {
                        authenticationPresenter.AddFundsAPI(addFundsApp);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                } else if (Deduction == 0) {
                    newActivationRequest = new NewActivationRequest();
                    newActivationRequest.setNumberOfDays(extras.getString("NumberOfDays"));
                    newActivationRequest.setSerialNumber(extras.getString("SerialNumber"));
                    newActivationRequest.setAmountCharged(extras.getString("AmountCharged"));
                    newActivationRequest.setRequestedForDtTm(extras.getString("RequestedForDtTm"));
                    newActivationRequest.setToken(userDetails.getTokenID());
                    newActivationRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
                    newActivationRequest.setRequestedDevice(getDeviceName());
                    newActivationRequest.setRequestedIP(IPaddress);
                    newActivationRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
                    try {
                        authenticationPresenter.activateSim(newActivationRequest);   //if 0
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                }
            } else if (userDetails.getRechargeStatus() == 0) {
                if ((Deduction > 0)) {
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
                } else if (userDetails.getRechargeStatus() == 0) {
                    //Extension Request api
                    NewExtensionRequest newExtensionRequest = new NewExtensionRequest();
                    newExtensionRequest.setNumberOfDays(extras.getString("NumberOfDaysR"));
                    newExtensionRequest.setMSISDN(extras.getString("MSISDN"));
                    newExtensionRequest.setAmountCharged(extras.getString("AmountChargedR"));
                    newExtensionRequest.setRequestedForDtTm(extras.getString("RequestedForDtTmR"));
                    newExtensionRequest.setToken(userDetails.getTokenID());
                    newExtensionRequest.setRefNo(userDetails.getTxnSeriesPrefix() + paypalTxnNumber);
                    newExtensionRequest.setRequestedDevice(getDeviceName());
                    newExtensionRequest.setRequestedIP(IPaddress);
                    newExtensionRequest.setRequestedOS("Android|" + userDetails.getLanguageSelect());
                    try {
                        authenticationPresenter.extensionRequest(newExtensionRequest);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                }
            }
        } catch (Exception e) {
            showToast(e.toString());
        }


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == m_paypalRequestCode) {
//            if (resultCode == Activity.RESULT_OK) {
//                assert data != null;
//                PaymentConfirmation configuration = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (configuration != null) {
//                    UserDetails userDetails = new UserDetails(this);
//                    String state = configuration.getProofOfPayment().getState();
//                    if (state.equals("approved")) {
//                        m_response.setText("payment Approved");
//                        updateFundReq.setId(updateFundID);
//                        updateFundReq.setPaypalReferenceID(configuration.getProofOfPayment().getTransactionId()); // send paypal id
//                        updateFundReq.setPayPalResponse("Payment Approved");
//                        updateFundReq.setRequestStatusID("15");
//                        updateFundReq.setTokenID(userDetails.getTokenID());
//                        updateFundReq.setTransactionReferenceID(sessionTxnID);
//                        try {
//                            authenticationPresenter.UpdateFundsMethod(updateFundReq);
//                        } catch (Exception e) {
//                            showToast(e.toString());
//                        }
//                        //Update Funds API
//                    } else {
//                        m_response.setText("not apporved");
//                        updateFundReq.setId(updateFundID);
//                        updateFundReq.setPaypalReferenceID(configuration.getProofOfPayment().getTransactionId());
//                        updateFundReq.setPayPalResponse("Not Approved");
//                        updateFundReq.setRequestStatusID("16");
//                        updateFundReq.setTransactionReferenceID(sessionTxnID);
//                        try {
//                            authenticationPresenter.UpdateFundsMethod(updateFundReq);
//                        } catch (Exception e) {
//                            showToast(e.toString());
//                        }
//                    }
//                } else {
//                    m_response.setText("confirmation is null");
//                    updateFundReq.setId(updateFundID);
//                    updateFundReq.setPaypalReferenceID(configuration.getProofOfPayment().getTransactionId());
//                    updateFundReq.setPayPalResponse("Confirmation is null");
//                    updateFundReq.setRequestStatusID("16");
//                    updateFundReq.setTransactionReferenceID(sessionTxnID);
//                    try {
//                        authenticationPresenter.UpdateFundsMethod(updateFundReq);
//                    } catch (Exception e) {
//                        showToast(e.toString());
//                    }
//                }
//
//
//            } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
//                Toast.makeText(this, "Invalid ", LENGTH_SHORT).show();
//        } else if (requestCode == Activity.RESULT_CANCELED)
//            Toast.makeText(this, "Cancel ", LENGTH_SHORT).show();
//
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showProgressBar();
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
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
                    Toast.makeText(mPayment.this, "amount+nonce error:" + e, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                Log.d("mylog", "user canceled");
                showToast(getApplication().getString(R.string.textCancel));
                hideProgressBar();
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("mylog", "Error : " + error.toString());
                showToast(getApplication().getString(R.string.textSorrySomethingwentwrong));
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

    Context context = this;

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
        } catch (Exception ignored) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
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
                        sentToSettings = true;
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
            super.onPostExecute(o);
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
                                UserDetails userDetails = new UserDetails(mPayment.this);
                                String x = response;
                                String str[] = x.split(",");
                                List<String> al;
                                al = Arrays.asList(str);
                                for (String s : al) {
                                    System.out.println(s);
                                }
                                String str1[] = al.get(0).split("=");
                                String tnxID = str1[1];
                                m_response.setText("payment Approved");
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
                                UserDetails userDetails = new UserDetails(mPayment.this);
                                String x = response;
                                String str[] = x.split("message=");
                                List<String> al;
                                al = Arrays.asList(str);
                                for (String s : al) {
                                    System.out.println(s);
                                }
                                String str1[] = al.get(1).split(",");
                                String error = str1[0];
//                                m_response.setText("not apporved");
                                updateFundReq.setId(updateFundID);
                                updateFundReq.setPaypalReferenceID("");   //not null ethe error
                                updateFundReq.setPayPalResponse("Not Approved");
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}

