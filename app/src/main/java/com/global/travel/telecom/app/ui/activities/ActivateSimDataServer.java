package com.global.travel.telecom.app.ui.activities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
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

public class ActivateSimDataServer extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    @SuppressLint("StaticFieldLeak")
    static EditText edtSerialNumber;
    EditText txtnoOfDays;
    TextView totalAmount, errorMsg, contactCare, OK;
    String Days = "0";
    Double rate = 0.0;
    Double Amount = 0.0;
    EditText todayDate;
    String token;
    String Serial;
    TextView scannerData;
    TextView validDateLeftAS;
    DatePickerDialog picker;
    Boolean SimValidAPIStatus = false;
    Boolean SpecialDealer = false;
    String TotalAmount;
    Context context = this;
    String timeFormat = "d MMMM,yyyy";
    androidx.appcompat.app.AlertDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_activate_sim_data_server;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        authenticationPresenter = new AuthenticationPresenter(this);
        todayDate = findViewById(R.id.datePickerActivateSimDataServer);
        edtSerialNumber = findViewById(R.id.editSerialDataServer);
        scannerData = findViewById(R.id.scannerDataServer);
//        edtSerialNumber.setOnFocusChangeListener(this);
        txtnoOfDays = findViewById(R.id.txtnoOfDaysActivateSimDataServer);
        validDateLeftAS = findViewById(R.id.validDateLeftASDataServer);
        UserDetails userDetails = new UserDetails(ActivateSimDataServer.this);
        token = userDetails.getTokenIDDataServer();
        totalAmount = findViewById(R.id.totalAmountActivateDataServer);
        Date date = new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        @SuppressLint("SimpleDateFormat") java.text.DateFormat df = new SimpleDateFormat(timeFormat);
        System.out.println("Date and time in US/Eastern: " + df.format(date));
        todayDate.setText(df.format(date));

        edtSerialNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edtSerialNumber.getText().toString().length() == 20) {
                    Serial = edtSerialNumber.getText().toString();
                    try {
                        authenticationPresenter.validateSim(Serial, token);
                    } catch (Exception e) {
                        showToast(e.getMessage());
                    }
//                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtnoOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Days = txtnoOfDays.getText().toString();
                txtnoOfDays.setHint("  ");
                try {
                    if (txtnoOfDays.getText().length() == 0) {
                        validDateLeftAS.setText("  ");
                        totalAmount.setText("");
                        totalAmount.setHint("$ 0.00");
                    } else if (SpecialDealer.equals(false)) {
                        Amount = ((Integer.parseInt(Days)) * rate);
                        txtnoOfDays.setFocusable(true);
                        NumberFormat formatter = NumberFormat.getNumberInstance();
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);
                        TotalAmount = formatter.format(Amount);
                        totalAmount.setText("$ " + TotalAmount);
                        getCurretDatePicker();
                    } else if (SpecialDealer.equals(true)) {
                        authenticationPresenter.GetRateForPaymentPlan(edtSerialNumber.getText().toString().trim(), Integer.parseInt(txtnoOfDays.getText().toString()), 1, "");
                    }
                } catch (Exception e) {
                    if (edtSerialNumber.getText().toString().isEmpty()) {
                        Toast.makeText(ActivateSimDataServer.this, getResources().getString(R.string.textPleaseEnterSIMSerialNumber), Toast.LENGTH_LONG).show();
                    } else if (Amount == 0) {
                        totalAmount.setText("$ 0.0");
                    } else
                        Toast.makeText(ActivateSimDataServer.this, getResources().getString(R.string.textInvalidNumberOfDay), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "simvalidated": {
                ActivateSimResponse obj = (ActivateSimResponse) response;
                try {
                    rate = obj.getmRatePerDay();
                    SimValidAPIStatus = true;

                    if (!obj.getmPromotionName().equals("")) {
                        SpecialDealer = true;
                        Days = obj.getNumberOfDays();
                        txtnoOfDays.setText(Days);
                        TotalAmount = "0";
                        showToast(obj.getResponseMessage());
                        getCurretDatePicker();

                    } else {
                        SpecialDealer = false;
                        showToast(obj.getResponseMessage());
                        txtnoOfDays.setEnabled(true);
                        txtnoOfDays.getText().clear();
                    }
                } catch (Exception e) {
                    txtnoOfDays.setText("0");
                    totalAmount.setText("$ 0.00");
                    ContactCarePopUp(obj.getResponseMessage(), getResources().getString(R.string.textcontactCare));   //here is show popup for invlid sim and conatc skygo team
                }
                break;
            }

            case "GetRateForPaymentPlan_SpecialPlan":
                GetRateForPaymentPlan obj = (GetRateForPaymentPlan) response;
                try {
                    SimValidAPIStatus = true;
                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    TotalAmount = formatter.format(obj.getRate());
                    totalAmount.setText("$ " + TotalAmount);
                    getCurretDatePicker();
                } catch (Exception e) {
                    txtnoOfDays.setText("0");
                    totalAmount.setText("$ 0.00");
                    showToast(e.getMessage());
                }
                break;
            case "GetRateForPaymentPlan_endUser":
                GetRateForPaymentPlan obj2 = (GetRateForPaymentPlan) response;
                try {
                    SimValidAPIStatus = true;
                    SpecialDealer = false;
                    rate = obj2.getRate();
                    Amount = ((Integer.parseInt(Days)) * rate);
                    txtnoOfDays.setFocusable(true);
                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    TotalAmount = formatter.format(Amount);
                    totalAmount.setText("$ " + TotalAmount);
                    getCurretDatePicker();
                } catch (Exception e) {
                    SimValidAPIStatus = false;
                    txtnoOfDays.setText("0");
                    totalAmount.setText("$ 0.00");
                    showToast(e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        edtSerialNumber.getText().clear();
        txtnoOfDays.getText().clear();
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(ActivateSimDataServer.this, getResources().getString(R.string.textNOInternetConnection), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ActivateSimDataServer.this, getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "simvalidated": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    showToast(getResources().getString(R.string.textPleaseLoginagain));
                    Intent intent = new Intent(ActivateSimDataServer.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    ContactCarePopUp(errorMessage, getResources().getString(R.string.textcontactCare));
                    txtnoOfDays.getText().clear();
                    edtSerialNumber.getText().clear();
                }
                break;
            }
            case "GetRateForPaymentPlan": {
                showToast(getResources().getString(R.string.textcontactCare));
                break;
            }
        }
    }

    public void btnBuyNowClickDataServer(View view) {
        try {
            if (TextUtils.isEmpty(edtSerialNumber.toString())) {
                throw new Exception(getResources().getString(R.string.textPleaseEnterSIMSerialNumber));
            } else if (edtSerialNumber.getText().length() != 20) {
                throw new Exception(getResources().getString(R.string.textInvalidSerialNumber));
            } else if (txtnoOfDays.getText().toString().isEmpty() ||
                    txtnoOfDays.getText().toString().equals("0") ||
                    txtnoOfDays.getText().toString().equals("00") ||
                    txtnoOfDays.getText().toString().equals("000")) {
                throw new Exception(getResources().getString(R.string.textPleaseEnterValidNumberOfDays));
            } else if (SimValidAPIStatus) {
                Days = txtnoOfDays.getText().toString();
                Serial = edtSerialNumber.getText().toString();
                Intent PaymentSummary = new Intent(ActivateSimDataServer.this, mPayment.class);
                PaymentSummary.putExtra("Number", Serial);
                PaymentSummary.putExtra("NumberOfDays", Days);
                PaymentSummary.putExtra("AmountCharged", TotalAmount);
                PaymentSummary.putExtra("RequestedForDtTm", todayDate.getText().toString());
                PaymentSummary.putExtra("AppPaymentType", "1");
                startActivity(PaymentSummary);
            }

        } catch (Exception e) {
            String[] Error = e.toString().split(":");
            showToast(Error[1]);
        }
    }

    public void scnnerafumctionDataServer(View view) {
        Intent i = new Intent(ActivateSimDataServer.this, ScannerFunction.class);
        i.putExtra("ActivityName","ActivateData");
        startActivity(i);
    }

    public void notificationButtonDataServer(View view) {
        Intent i = new Intent(ActivateSimDataServer.this, Notification.class);
        startActivity(i);
    }

    public void backToDashboardButtonDataServer(View view) {
        finish();
    }

    public void getCurretDatePickerDataServer(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(ActivateSimDataServer.this, R.style.DatePickerDialog,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    String dateConverter = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    try {
                        @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateConverter);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
                        assert date1 != null;
                        String strDate = formatter.format(date1);
                        todayDate.setText(strDate);
                        getCurretDatePicker();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, year, month, day);

        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();

    }

    public void calenderButtonDataServer(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        picker = new DatePickerDialog(ActivateSimDataServer.this, R.style.DatePickerDialog,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    String dateConverter = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    try {
                        @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateConverter);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
                        assert date1 != null;
                        String strDate = formatter.format(date1);
                        todayDate.setText(strDate);
                        getCurretDatePicker();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    @SuppressLint("SetTextI18n")
    public void getCurretDatePicker() {
        String getDate = todayDate.getText().toString();
        try {
            @SuppressLint("SimpleDateFormat") Date date1 = new SimpleDateFormat(timeFormat).parse(getDate);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d MMMM");
            assert date1 != null;
            String validityStartDate = formatter.format(date1);
            String getNoOfDays = txtnoOfDays.getText().toString();
            if (getNoOfDays.equals("00") || getNoOfDays.equals("0") || getNoOfDays.equals("000") || getNoOfDays.contains(" ")) {
                validDateLeftAS.setText("  ");

            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.add(Calendar.DATE, Integer.parseInt(getNoOfDays) - 1);   //add -1 for date change (6th march 2020)
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("d MMMM");
                String validityEndDate = sdf1.format(cal.getTime());
                validDateLeftAS.setText(getResources().getString(R.string.textValidity) + " (" + validityStartDate + " " + getResources().getString(R.string.textto) + " " + validityEndDate + " )");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void addOnDataServer(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://orders.skygowifi.com"));
        startActivity(browserIntent);
    }

    private void ContactCarePopUp(String errorName, String ContactCare) {
        try {
            @SuppressLint("InflateParams") View contactCarePopUp = LayoutInflater.from(this).inflate(R.layout.dialog_validation_popup, null);
            androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(contactCarePopUp);
            progressDialog = mBuilder.create();
            Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            progressDialog.show();

            //ID
            errorMsg = contactCarePopUp.findViewById(R.id.errorMsg);
            contactCare = contactCarePopUp.findViewById(R.id.contactCare);
            OK = contactCarePopUp.findViewById(R.id.OK);
            errorMsg.setText(errorName);

            OK.setOnClickListener(v -> progressDialog.dismiss());

        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }
}
