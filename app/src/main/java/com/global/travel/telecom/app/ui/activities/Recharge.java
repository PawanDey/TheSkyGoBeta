package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.GetRateForPaymentPlan;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static android.widget.Toast.LENGTH_LONG;

//import android.icu.text.SimpleDateFormat;

public class Recharge extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    EditText edtTextMSISDN;
    TextView totalAmountRecharge;
    EditText numberOfDaysRecharge;
    String Days = "0";
    double rate = 0.0;
    double Amount = 0.0;
    public BaseView baseView;
    TextView todayDate, errorMsg, contactCare, OK;
    String token;
    String MSISDN;
    TextView GoodUntil;
    String validityStartDate;
    Boolean SpecialDealer = false;
    Boolean SimValidAPIStatus = false;
    androidx.appcompat.app.AlertDialog progressDialog;


    @Override
    protected int getLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        GoodUntil = findViewById(R.id.validDateLeftRC);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        authenticationPresenter = new AuthenticationPresenter(this);
        todayDate = (TextView) findViewById(R.id.datePickerRecharge);
        totalAmountRecharge = findViewById(R.id.totalAmountRecharge);
        final UserDetails userDetails = new UserDetails(Recharge.this);
        token = userDetails.getTokenID();
        edtTextMSISDN = findViewById(R.id.edtMSISDN);
        edtTextMSISDN.setText(userDetails.getMSISDN());
        edtTextMSISDN.setFocusable(false);
        numberOfDaysRecharge = findViewById(R.id.txtnumberOfDaysRecharge);
        try {
            MSISDN = edtTextMSISDN.getText().toString();
            authenticationPresenter.validateMSISDN(MSISDN, token);
        } catch (Exception e) {
            showToast(e.toString());
        }
        try {
            authenticationPresenter.GetRateForPaymentPlan("", 0, 2, userDetails.getMSISDN());
        } catch (Exception e) {
            e.printStackTrace();
        }

        numberOfDaysRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Days = numberOfDaysRecharge.getText().toString();
                try {
                    if (numberOfDaysRecharge.getText().length() == 0) {
                        GoodUntil.setText("  ");
                        totalAmountRecharge.setText("");
                        totalAmountRecharge.setHint("$ 0.00");

                    } else if (SpecialDealer.equals(false)) {
                        Amount = ((Integer.parseInt(Days)) * rate);
                        NumberFormat formatter = NumberFormat.getNumberInstance();
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);
                        String TotalAmount = formatter.format(Amount);
                        totalAmountRecharge.setText("$ " + TotalAmount);
                        getCurretDatePicker();
                    } else if (SpecialDealer.equals(true)) {
                        showToast("Special dealer");
                        authenticationPresenter.GetRateForPaymentPlan("", Integer.parseInt(numberOfDaysRecharge.getText().toString()), 2, userDetails.getMSISDN());

                    } else {
                        totalAmountRecharge.setText("");
                        GoodUntil.setText("");
                        totalAmountRecharge.setText("");
                        Toast.makeText(Recharge.this, R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (numberOfDaysRecharge.getText().toString().isEmpty()) {
                        Toast.makeText(Recharge.this, R.string.textPleaseEnterValidNumberOfDays, LENGTH_LONG).show();
                        totalAmountRecharge.setText("$ 0.0");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        numberOfDaysRecharge.getText().clear();
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(String method3, Object response) {
        switch (method3) {
            case "rechargeMSISDN": {
                ActivateSimResponse obj = (ActivateSimResponse) response;
                try {
                    rate = obj.getmRatePerDay();
                    String dateConverter = obj.getmLastValidityDate();
                    Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(dateConverter);
                    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM,yyyy");
                    String strDate = formatter.format(date1);
                    todayDate.setText(strDate);
                    UserDetails userDetails = new UserDetails(this);
                    userDetails.setRechargeStatus(0);
                    numberOfDaysRecharge.getText().clear();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case "GetRateForPaymentPlan_SpecialPlan": {
                GetRateForPaymentPlan obj = (GetRateForPaymentPlan) response;
                try {
                    SimValidAPIStatus = true;
                    SpecialDealer = true;
                    totalAmountRecharge.setText("$ " + obj.getRate());
                    getCurretDatePicker();
                } catch (Exception e) {
                    totalAmountRecharge.setText("");
                    totalAmountRecharge.setText("$ 0.00");
                    showToast(e.getMessage());
                }
                break;
            }

            case "GetRateForPaymentPlan_endUser": {
                GetRateForPaymentPlan obj2 = (GetRateForPaymentPlan) response;
                try {
                    SimValidAPIStatus = true;
                    rate = obj2.getRate();
                    Amount = ((Integer.parseInt(Days)) * rate);
                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    String TotalAmount = formatter.format(Amount);
                    totalAmountRecharge.setText("$ " + TotalAmount);
                    getCurretDatePicker();
                } catch (Exception e) {
                    totalAmountRecharge.setText("");
                    numberOfDaysRecharge.getText().clear();
                    showToast(e.getMessage());
                }
                break;
            }
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "rechargeMSISDN": {
                SimValidAPIStatus = false;
                ContactCarePopUp(errorMessage, getApplication().getString(R.string.textcontactCare));  //here is add popup scrrren to show the popoupmsg for any error
                break;
            }

            case "GetRateForPaymentPlan": {
                SimValidAPIStatus = false;
                break;
            }
        }
    }

    public void btnBuyNowClick(View view) {
        UserDetails userDetails = new UserDetails(this);
        try {
            if (edtTextMSISDN.getText().toString().isEmpty()) {
//                throw new Exception("Please Enter SIM Serial Number");
                throw new Exception(getResources().getString(R.string.textPleaseEnterSIMSerialNumber));
            } else if (numberOfDaysRecharge.getText().toString().equals("0") || numberOfDaysRecharge.getText().toString().isEmpty()) {
//                throw new Exception("Please Enter Valid Number Of Days");
                throw new Exception(getResources().getString(R.string.textPleaseEnterValidNumberOfDays));
            } else if (SimValidAPIStatus) {
                Days = numberOfDaysRecharge.getText().toString();
                MSISDN = edtTextMSISDN.getText().toString();
                Intent paymnetSummaryR = new Intent(Recharge.this, mPayment.class);
                paymnetSummaryR.putExtra("MSISDN", MSISDN);
                paymnetSummaryR.putExtra("NumberOfDaysR", Days);
                paymnetSummaryR.putExtra("RequestedIPR", "24");
                paymnetSummaryR.putExtra("RequestedOSR", "Android|" + userDetails.getLanguageSelect());
                paymnetSummaryR.putExtra("AmountChargedR", String.valueOf(Amount));
                paymnetSummaryR.putExtra("RequestedForDtTmR", validityStartDate);
                paymnetSummaryR.putExtra("RefNoR", "1");
                paymnetSummaryR.putExtra("RequestedDeviceR", "Android|" + userDetails.getLanguageSelect());
                startActivity(paymnetSummaryR);
            }
        } catch (Exception e) {
            String[] Error = e.toString().split(":");
            showToast(Error[1]);
        }

    }

    public void backToDashboardButton(View view) {
        finish();
    }

    public void notificationButton(View view) {
        Intent i = new Intent(this, Notification.class);
        startActivity(i);
    }

    public void getCurretDatePicker() {
        String getDate = todayDate.getText().toString();
        try {

            Date date1 = new SimpleDateFormat("d MMMM,yyyy").parse(getDate);
            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM");
            validityStartDate = formatter.format(date1);

            String getNoOfDays = numberOfDaysRecharge.getText().toString();
            if (getNoOfDays.equals("00") || getNoOfDays.equals("0") || getNoOfDays.equals(null) || getNoOfDays.contains(" ") || getNoOfDays.equals("000")) {
                GoodUntil.setText("  ");

            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.add(Calendar.DATE, Integer.parseInt(getNoOfDays));
                SimpleDateFormat sdf1 = new SimpleDateFormat("d MMMM");
                String validityEndDate = sdf1.format(cal.getTime());
                GoodUntil.setText(getResources().getString(R.string.textValidity) + " (" + validityStartDate + " " + getResources().getString(R.string.textto) + " " + validityEndDate + " )");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;
    Context context = this;

    public void hotspotButton(View view) {
        try {
            Hotspot hotspot = new Hotspot();
            hotspot.hotspotFxn(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOn(View view) {
        setURL("https://orders.skygowifi.com");
    }

    private void setURL(String getURL) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
        startActivity(browserIntent);
    }

    private void ContactCarePopUp(String errorName, String ContactCare) {
        try {
            View contactCarePopUp = LayoutInflater.from(this).inflate(R.layout.dialog_validation_popup, null);
            androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(contactCarePopUp);
            progressDialog = mBuilder.create();
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            progressDialog.show();

            //ID
            errorMsg = contactCarePopUp.findViewById(R.id.errorMsg);
            contactCare = contactCarePopUp.findViewById(R.id.contactCare);
            OK = contactCarePopUp.findViewById(R.id.OK);
            errorMsg.setText(errorName);

            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.dismiss();
                }
            });

        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }
}
