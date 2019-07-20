package com.gtt.app.ui.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gtt.app.R;
import com.gtt.app.base.BaseActivity;
import com.gtt.app.base.BaseView;
import com.gtt.app.model.ActivateSimResponse;
import com.gtt.app.model.NewExtensionRequest;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class Recharge extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    EditText edtTextMSISDN;
    TextView totalAmountRecharge;
    EditText numberOfDaysRecharge;
    String Days = "0";
    double rate = 0.0;
    double Amount = 0.0;
    public BaseView baseView;
    TextView todayDate;
    String token;
    String MSISDN;
    TextView GoodUntil;
    String validityStartDate;
    SimpleDateFormat formatter;
    //    Button MSISDNRecharge;

    @Override
    protected int getLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        GoodUntil = findViewById(R.id.validDateLeftRC);
        authenticationPresenter = new AuthenticationPresenter(this);
        todayDate = (TextView) findViewById(R.id.datePickerRecharge);
        totalAmountRecharge = findViewById(R.id.totalAmountRecharge);
        UserDetails userDetails = new UserDetails(Recharge.this);
        token = userDetails.getTokenID();
        edtTextMSISDN = findViewById(R.id.edtMSISDN);
        edtTextMSISDN.setText(userDetails.getMSISDN());
        edtTextMSISDN.setFocusable(false);
        numberOfDaysRecharge = findViewById(R.id.txtnumberOfDaysRecharge);
        UserDetails userDetails1 = new UserDetails(this);
        try {
            Bundle extras = getIntent().getExtras();
            MSISDN = edtTextMSISDN.getText().toString();
            authenticationPresenter.validateMSISDN(MSISDN, token);

        } catch (Exception e) {
            showToast(e.toString());
        }

        numberOfDaysRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Days = numberOfDaysRecharge.getText().toString();
                try {
                    if (numberOfDaysRecharge.getText().length() == 0) {
                        GoodUntil.setText("  ");
                        totalAmountRecharge.setText(" ");
                        totalAmountRecharge.setHint("$ 0.00");

                    } else {

                        Amount = ((Integer.parseInt(Days)) * rate);
                        NumberFormat formatter = NumberFormat.getNumberInstance();
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);
                        String TotalAmount = formatter.format(Amount);
                        totalAmountRecharge.setText("$ "+TotalAmount);
                        getCurretDatePicker();
                    }
                } catch (Exception e) {
                    if (edtTextMSISDN.getText().toString().isEmpty()) {
//                        showToast("Please Enter SIM Serial Number");
                        Toast.makeText(Recharge.this, R.string.textPleaseEnterSIMSerialNumber, Toast.LENGTH_LONG).show();
                    } else
//                        showToast("Invalid Number Of Day");
                        Toast.makeText(Recharge.this, R.string.textInvalidNumberOfDay, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onFailure() {
//        showToast("Sorry! Something went wrong");
        Toast.makeText(Recharge.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(String method3, Object response) {
        switch (method3) {
            case "rechargeMSISDN": {
                ActivateSimResponse obj = (ActivateSimResponse) response;


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        rate = Double.parseDouble(obj.getmRatePerDay());
                        String dateConverter = obj.getmLastValidityDate();
                        Date date1 = new SimpleDateFormat("dd-MMM-yyyy").parse(dateConverter);
                        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM,yyyy");
                        String strDate = formatter.format(date1);
                        todayDate.setText(strDate);
                        UserDetails userDetails = new UserDetails(this);
                        userDetails.setRechargeStatus(0);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "rechargeMSISDN": {
                showToast(errorMessage);
            }
        }
    }

    public void btnBuyNowClick(View view) {
        UserDetails userDetails = new UserDetails(this);
        try {
            if (edtTextMSISDN.getText().toString().isEmpty()) {
//                throw new Exception("Please Enter SIM Serial Number");
                throw new Exception(getResources().getString(R.string.textPleaseEnterSIMSerialNumber));
            }
            if (numberOfDaysRecharge.getText().toString().equals("0") || numberOfDaysRecharge.getText().toString().isEmpty()) {
//                throw new Exception("Please Enter Valid Number Of Days");
                throw new Exception(getResources().getString(R.string.textPleaseEnterValidNumberOfDays));
            } else {
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
                paymnetSummaryR.putExtra("RequestedDeviceR", "2");
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

    public void notificatioButton(View view) {
        Intent i = new Intent(this, Notification.class);
        startActivity(i);
    }

    public void getCurretDatePicker() {
        String getDate = todayDate.getText().toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
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
    }
}
