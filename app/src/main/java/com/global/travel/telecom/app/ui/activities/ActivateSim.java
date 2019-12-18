package com.global.travel.telecom.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
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

public class ActivateSim extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    static EditText edtSerialNumber;
    EditText txtnoOfDays;
    TextView totalAmount,errorMsg,contactCare,OK;
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
        return R.layout.activity_activate_sim;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        authenticationPresenter = new AuthenticationPresenter(this);
        todayDate = findViewById(R.id.datePickerActivateSim);
        edtSerialNumber = findViewById(R.id.editSerial);
        scannerData = findViewById(R.id.scanner);
//        edtSerialNumber.setOnFocusChangeListener(this);
        txtnoOfDays = findViewById(R.id.txtnoOfDaysActivateSim);
        validDateLeftAS = findViewById(R.id.validDateLeftAS);
        UserDetails userDetails = new UserDetails(ActivateSim.this);
        token = userDetails.getTokenID();
        totalAmount = findViewById(R.id.totalAmountActivate);
        Date date = new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        java.text.DateFormat df = new SimpleDateFormat(timeFormat);
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
//                        showToast("Special dealer");
                        authenticationPresenter.GetRateForPaymentPlan(edtSerialNumber.getText().toString().trim(), Integer.parseInt(txtnoOfDays.getText().toString()), 1, "");
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
                        txtnoOfDays.setEnabled(true);
                        txtnoOfDays.getText().clear();
                    }
                } catch (Exception e) {
                    txtnoOfDays.setText("0");
                    totalAmount.setText("$ 0.00");
                    ContactCarePopUp(obj.getResponseMessage(),getResources().getString(R.string.textcontactCare));   //here is show popup for invlid sim and conatc skygo team
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
                    ContactCarePopUp(errorMessage,getResources().getString(R.string.textcontactCare));
                    txtnoOfDays.getText().clear();
                    edtSerialNumber.getText().clear();
                }
                break;
            }
            case "GetRateForPaymentPlan": {
                showToast("Please Contact Customer Care: support@theskygo.com");  //here is show popup for invalid sim or contact to skygo team
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
                            SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
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
                            SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
                            assert date1 != null;
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
            Date date1 = new SimpleDateFormat(timeFormat).parse(getDate);
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
            Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
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



