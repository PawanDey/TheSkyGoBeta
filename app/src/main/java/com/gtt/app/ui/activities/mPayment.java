package com.gtt.app.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.aware.WifiAwareManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gtt.app.R;
import com.gtt.app.base.BaseActivity;
import com.gtt.app.model.ActivateSimResponse;
import com.gtt.app.model.AddFundsApp;
import com.gtt.app.model.AddFundsResponse;
import com.gtt.app.model.LoginResponse;
import com.gtt.app.model.NewActivationRequest;
import com.gtt.app.model.NewExtensionRequest;
import com.gtt.app.model.UpdateFundReq;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.lang.reflect.Method;
import java.math.BigDecimal;


import java.text.NumberFormat;
import java.util.Random;


public class mPayment extends BaseActivity {

    Button payPalPaymentButton, modifyButton;
    TextView m_response;
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AYM1ES72_Yc1GwxAeGMhRGpQSEFWRVt-JvfjRN54O466vcO9tB8HgGR1WWaLDq4Cc-a0wBjyM-5TN9qu";
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

    @Override
    protected int getLayout() {
        return R.layout.activity_m_payment;
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "activateSim": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    showToast("Please Login again");
                    Intent intent = new Intent(mPayment.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast(errorMessage);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_payment);
        addFundsApp = new AddFundsApp();
        Bundle extras = getIntent().getExtras();
        authenticationPresenter = new AuthenticationPresenter(this);
        SimNumber = findViewById(R.id.simNumber);
        Amount = findViewById(R.id.textView7);
        CartAmount = findViewById(R.id.textView9);
        Convenience = findViewById(R.id.textView11);
        AmountPayabale = findViewById(R.id.editAmount1);
        NumberOfDays = findViewById(R.id.textView17);
        ActivationDate = findViewById(R.id.textView);
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
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
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
                userDetails.setRechargeStatus(0);
                Intent i = new Intent(mPayment.this, paymentSucessfull.class);
                startActivity(i);
                break;
            }

            case "ExtensionRequest": {
                Intent paymentsuccess = new Intent(mPayment.this, paymentSucessfull.class);
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
                    newActivationRequest.setRequestedDevice(extras.getString("RequestedDevice"));
                    newActivationRequest.setRequestedIP(extras.getString("RequestedIP"));
                    newActivationRequest.setRequestedOS(extras.getString("RequestedOS"));
                    try {
                        authenticationPresenter.activateSim(newActivationRequest);
                    } catch (Exception e) {
                        showToast(e.toString());
                    }
                } else if (userDetails.getRechargeStatus() == 0) {
                    //Extension Request api
                    NewExtensionRequest newExtensionRequest = new NewExtensionRequest();
                    newExtensionRequest.setNumberOfDays(extras.getString("NumberOfDaysR"));
                    newExtensionRequest.setMSISDN(extras.getString("SerialNumber"));
                    newExtensionRequest.setAmountCharged(extras.getString("AmountChargedR"));
                    newExtensionRequest.setRequestedForDtTm(extras.getString("RequestedForDtTmR"));
                    newExtensionRequest.setToken(userDetails.getTokenID());
                    newExtensionRequest.setRefNo(extras.getString("RefNoR"));
                    newExtensionRequest.setRequestedDevice(extras.getString("RequestedDeviceR"));
                    newExtensionRequest.setRequestedIP(extras.getString("RequestedIPR"));
                    newExtensionRequest.setRequestedOS(extras.getString("RequestedOSR"));
                    break;
                }
            }

            case "AddFundsViaAPP": {
                try {
                    AddFundsResponse addFundsResponse = (AddFundsResponse) response;
                    Bundle extras = getIntent().getExtras();
                    updateFundID = addFundsResponse.getRequestId();
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(extras.getString("AmountCharged")), "USD", "Test Paypal",
                            PayPalPayment.PAYMENT_INTENT_SALE);
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

            Deduction = Double.parseDouble(extras.getString("AmountCharged"));
            if ((Deduction > 0)) {
                UserDetails userDetails = new UserDetails(this);
                String paypalTxnNumber = String.format("%06d", random.nextInt(1000000));
                sessionTxnID = userDetails.getTxnSeriesPrefix() + paypalTxnNumber;
                addFundsApp.setAmountCharged(String.valueOf(Deduction));
                addFundsApp.setDealerID("0");
                addFundsApp.setIMEI("1");
                addFundsApp.setLatitude("");
                addFundsApp.setLongitude("");
                addFundsApp.setMacID("");
                addFundsApp.setPayPalRefNo("");
                addFundsApp.setPaymentMode("2");
                addFundsApp.setRemarks("Payment From Android APP");
                addFundsApp.setRequestedDevice("Mobile");
                addFundsApp.setRequestedIP("");
                addFundsApp.setRequestedOS("Android");
                addFundsApp.setServiceCharge("0");
                addFundsApp.setTokenID(userDetails.getTokenID());
                addFundsApp.setTransactionReferenceID(sessionTxnID);
                addFundsApp.setTransactionType("0");
                try {
                    authenticationPresenter.AddFundsAPI(addFundsApp);
                }
                catch (Exception e)
                {
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
                newActivationRequest.setRequestedIP(extras.getString("RequestedIP"));
                newActivationRequest.setRequestedOS(extras.getString("RequestedOS"));
                try {
                    authenticationPresenter.activateSim(newActivationRequest);
                }
                catch (Exception e)
                {
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
                        updateFundReq.setPaypalReferenceID(sessionTxnID);
                        updateFundReq.setPayPalResponse("payment Approved");
                        updateFundReq.setRequestStatusID("15");
                        updateFundReq.setTokenID(userDetails.getTokenID());
                        updateFundReq.setTransactionReferenceID(sessionTxnID);
                        try {
                            authenticationPresenter.UpdateFundsMethod(updateFundReq);
                        }
                        catch (Exception e)
                        {
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
                        }
                        catch (Exception e)
                        {
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
                    }
                    catch (Exception e)
                    {
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
        showToast("Sorry! Something went wrong");
    }

    public void notificatioButton(View view) {
        Intent i = new Intent(mPayment.this, Notification.class);
        startActivity(i);
    }

    public void backToDashboardButton(View view) {
        finish();
    }

    public void backToactivation(View view){
        finish();
    }

}

