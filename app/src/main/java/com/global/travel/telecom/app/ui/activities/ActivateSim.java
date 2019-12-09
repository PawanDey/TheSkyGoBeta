package com.global.travel.telecom.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.GetRateForPaymentPlan;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import android.icu.text.SimpleDateFormat;


public class ActivateSim extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    static EditText edtSerialNumber;
    EditText txtnoOfDays;
    TextView totalAmount;
    String Days = "0";
    Double rate = 0.0;
    Double Amount = 0.0;
    //    public BaseView baseView;
    EditText todayDate;
    String token;
    String Serial;
    NewActivationRequest newActivationRequest;
    TextView scannerData;
    //    Button scannerDataLogo;
    TextView ValidDays, validDateLeftAS;
    DatePickerDialog picker;
    Boolean ignoreChange = false;
    Boolean SimValidAPIStatus = false;
    Boolean SpecialDealer = false;
    String TotalAmount;
    Context context = this;
    Date d = new Date();
    CharSequence DateToday = DateFormat.format("d MMMM,yyyy", d.getTime());

    @Override
    protected int getLayout() {
        return R.layout.activity_activate_sim;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        // authenticationPresenter = new AuthenticationPresenter(this);
        authenticationPresenter = new AuthenticationPresenter(this);
        todayDate = findViewById(R.id.datePickerActivateSim);
        todayDate.setText(DateToday);
        ValidDays = findViewById(R.id.validDateLeftAS);
        edtSerialNumber = findViewById(R.id.editSerial);
        scannerData = findViewById(R.id.scanner);
//        edtSerialNumber.setOnFocusChangeListener(this);
        txtnoOfDays = findViewById(R.id.txtnoOfDaysActivateSim);
        validDateLeftAS = findViewById(R.id.validDateLeftAS);
        UserDetails userDetails = new UserDetails(ActivateSim.this);
        token = userDetails.getTokenID();
//        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJJZCI6ImFiYyIsIm5iZiI6MTU3NDk0NjM3MiwiZXhwIjoxNTc0OTgyMzcyLCJpYXQiOjE1NzQ5NDYzNzJ9.FYEgVkQQsKOMm3CZlWISGeTiGrxb5n9CS-oscRQomF0;";
        totalAmount = findViewById(R.id.totalAmountActivate);
        WifiManager.LocalOnlyHotspotReservation mReservation;
        Context context = this;

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

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Days = txtnoOfDays.getText().toString();
                txtnoOfDays.setHint("  ");
                try {
                    if (ignoreChange == false) {
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
//                            showToast("Special dealer");
                            authenticationPresenter.GetRateForPaymentPlan(edtSerialNumber.getText().toString().trim(), Integer.parseInt(txtnoOfDays.getText().toString()));
                        }
                    }
                } catch (Exception e) {
                    if (edtSerialNumber.getText().toString().isEmpty()) {
                        Toast.makeText(ActivateSim.this, R.string.textPleaseEnterSIMSerialNumber, Toast.LENGTH_LONG).show();
                    } else if (Amount == 0) {
                        totalAmount.setText("$ 0.0");
                    } else
                        Toast.makeText(ActivateSim.this, R.string.textInvalidNumberOfDay, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        txtnoOfDays.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "simvalidated": {
                ActivateSimResponse obj = (ActivateSimResponse) response;
                try {
                    rate = obj.getmRatePerDay();
                    SimValidAPIStatus = true;

                    if (Integer.parseInt(obj.getNumberOfDays()) > 0) {
                        SpecialDealer = true;
                        Days = obj.getNumberOfDays();
                        Amount = 0.0;
                        txtnoOfDays.setText(Days);
                        totalAmount.setText("$ 0.0");
                        TotalAmount = "0";
                        showToast(obj.getResponseMessage());
                        getCurretDatePicker();

                    } else {
                        showToast(obj.getResponseMessage());
                        ignoreChange = false;
                        txtnoOfDays.setEnabled(true);
                        txtnoOfDays.getText().clear();//
                        // Days = txtnoOfDays.getText().toString();
//                        Amount = ((Integer.parseInt(Days)) * rate);
//                        NumberFormat formatter = NumberFormat.getNumberInstance();
//                        formatter.setMinimumFractionDigits(2);
//                        formatter.setMaximumFractionDigits(2);
//                        String TotalAmount = formatter.format(Amount);
//                        totalAmount.setText("$" + TotalAmount);
//                        showToast(obj.getResponseMessage());
                    }
                } catch (Exception e) {
                    txtnoOfDays.setText("0");
                    totalAmount.setText("$ 0.00");
                    showToast(e.toString());
                }
                break;
            }

            case "GetRateForPaymentPlan":
                GetRateForPaymentPlan obj = (GetRateForPaymentPlan) response; //change pojo classs name model
                try {
                    totalAmount.setText("$ " + obj.getRate());
                } catch (Exception e) {
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
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(ActivateSim.this, R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ActivateSim.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "simvalidated": {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    showToast("Please Login again");

                    Intent intent = new Intent(ActivateSim.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast(errorMessage);
                    txtnoOfDays.getText().clear();
                    edtSerialNumber.getText().clear();
                    ignoreChange = false;
                }
                break;
            }
            case "GetRateForPaymentPlan": {
                showToast("Please Contact Customer Care: support@theskygo.com");
                break;
            }
        }
    }

    public void btnBuyNowClick(View view) {
        try {
            UserDetails userDetails = new UserDetails(this);
            if (TextUtils.isEmpty(edtSerialNumber.toString())) {
//                throw new Exception(""+R.string.textPleaseEnterSIMSerialNumber);
                throw new Exception(getResources().getString(R.string.textPleaseEnterSIMSerialNumber));
            } else if (edtSerialNumber.getText().length() != 20) {
//                throw new Exception(""+R.string.textInvalidSerialNumber);
                throw new Exception(getResources().getString(R.string.textInvalidSerialNumber));
            } else if (txtnoOfDays.getText().toString().isEmpty() ||
                    txtnoOfDays.getText().toString().equals("0") ||
                    txtnoOfDays.getText().toString().equals("00") ||
                    txtnoOfDays.getText().toString().equals("000")) {
//                throw new Exception(""+R.string.textPleaseEnterValidNumberOfDays);
                throw new Exception(getResources().getString(R.string.textPleaseEnterValidNumberOfDays));
            } else if (SimValidAPIStatus) {

                Days = txtnoOfDays.getText().toString();
                Serial = edtSerialNumber.getText().toString();
                Intent PaymentSummary = new Intent(ActivateSim.this, mPayment.class);
                PaymentSummary.putExtra("SerialNumber", Serial);
                PaymentSummary.putExtra("NumberOfDays", Days);
                PaymentSummary.putExtra("RequestedIP", "ipAddress");
                PaymentSummary.putExtra("RequestedOS", getDeviceName());
                PaymentSummary.putExtra("AmountCharged", TotalAmount);
                PaymentSummary.putExtra("RequestedForDtTm", todayDate.getText().toString());
                PaymentSummary.putExtra("RefNo", "1");
                PaymentSummary.putExtra("RequestedDevice", "Android|" + userDetails.getLanguageSelect());
                startActivity(PaymentSummary);
            }
        } catch (Exception e) {
            String Error[] = e.toString().split(":");
            showToast(Error[1]);
        }
    }

    public void scnnerafumction(View view) {
        Intent i = new Intent(ActivateSim.this, ScannerFunction.class);
        startActivity(i);
    }

    public void notificationButton(View view) {
        Intent i = new Intent(ActivateSim.this, Notification.class);
        startActivity(i);
    }

    public void backToDashboardButton(View view) {
        finish();
    }

    public void getCurretDatePicker(View view) throws ParseException {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog

        picker = new DatePickerDialog(ActivateSim.this, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateConverter = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateConverter);
                            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM,yyyy");
                            String strDate = formatter.format(date1);
                            todayDate.setText(strDate);
                            getCurretDatePicker();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, year, month, day);

        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();

    }

    public void calenderButton(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        picker = new DatePickerDialog(ActivateSim.this, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateConverter = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateConverter);
                            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM,yyyy");
                            String strDate = formatter.format(date1);
                            todayDate.setText(strDate);
                            getCurretDatePicker();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    public void getCurretDatePicker() {
        String getDate = todayDate.getText().toString();
        try {
            Date date1 = new SimpleDateFormat("d MMMM,yyyy").parse(getDate);
            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM");
            String validityStartDate = formatter.format(date1);
            String getNoOfDays = txtnoOfDays.getText().toString();
            if (getNoOfDays.equals("00") || getNoOfDays.equals("0") || getNoOfDays.contains(" ") || getNoOfDays.equals(null)) {
                validDateLeftAS.setText("  ");

            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.add(Calendar.DATE, Integer.parseInt(getNoOfDays));
                SimpleDateFormat sdf1 = new SimpleDateFormat("d MMMM");
                String validityEndDate = sdf1.format(cal.getTime());
                validDateLeftAS.setText(getResources().getString(R.string.textValidity) + " (" + validityStartDate + " " + getResources().getString(R.string.textto) + " " + validityEndDate + " )");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void noOFdaysHide(View view) {
    }

    public String getDeviceName() {
        String MANUFACTURER = Build.MANUFACTURER;
        String model = Build.MODEL;
        return MANUFACTURER + "__" + model;
    }
}



