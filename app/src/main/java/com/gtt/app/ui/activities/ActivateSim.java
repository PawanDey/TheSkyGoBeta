package com.gtt.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gtt.app.R;
import com.gtt.app.base.BaseActivity;
import com.gtt.app.base.BaseView;
import com.gtt.app.model.ActivateSimResponse;
import com.gtt.app.model.NewActivationRequest;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.exit;

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
    TextView ValidDays;
    DatePickerDialog picker;
    Boolean ignoreChange =false;




    @Override
    protected int getLayout() {
        return R.layout.activity_activate_sim;
    }


    Date d = new Date();
    CharSequence DateToday = DateFormat.format("MMMM d, yyyy ", d.getTime());

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
        UserDetails userDetails = new UserDetails(ActivateSim.this);
        token = userDetails.getTokenID();
        totalAmount = findViewById(R.id.totalAmountActivate);

        edtSerialNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtSerialNumber.getText().toString().length() == 20) {

                    Serial = edtSerialNumber.getText().toString();
                    if (Serial != null && !Serial.isEmpty()) {

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            showToast("Sorry! Something went wrong");
                                            return;
                                        }
                                        try {
                                            authenticationPresenter.validateSim(Serial, token);
                                        }catch (Exception e)
                                        {
                                            showToast(e.toString());
                                        }

                                    }
                                });
                    }
                }
                else
                {
//                 txtnoOfDays.getText().clear();
                    totalAmount.setText("$ 0.0");

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
                try {
                    if(ignoreChange==false)
                    {
                        Amount = ((Integer.parseInt(Days)) * rate);
                        txtnoOfDays.setFocusable(true);
                        NumberFormat formatter = NumberFormat.getNumberInstance();
                        formatter.setMinimumFractionDigits(2);
                        formatter.setMaximumFractionDigits(2);
                        String TotalAmount = formatter.format(Amount);
                        totalAmount.setText("$" + TotalAmount);

                    }
                }
                catch (Exception e)
                {
                    if (edtSerialNumber.getText().toString().isEmpty())
                    {
                        showToast("Please Enter SIM Serial Number");
                    }
                    else if(Amount==0)
                    {
                        totalAmount.setText("$ 0.0");
                    }
                    else
                        showToast("Invalid Number Of Day");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//
//        txtnoOfDays.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    @Override
    public void onFailure() {
        showToast("Sorry! Something went wrong");
    }

    @Override
    public void onSuccess(String method2, Object response) {
        switch (method2) {
            case "simvalidated": {
                ActivateSimResponse obj = (ActivateSimResponse) response;


                try {
//
                    rate = Double.parseDouble(obj.getmRatePerDay());

                    if (!obj.getNumberOfDays().equals("0")) {
                        Days = obj.getNumberOfDays();
                        txtnoOfDays.setFocusable(false);
                        Amount = 0.0;
                        ignoreChange=true;
                        txtnoOfDays.setText(Days);
                        totalAmount.setText("$ 0.0");

                        showToast(obj.getResponseMessage());
                    }
                    else
                    {
                        showToast(obj.getResponseMessage());
                        ignoreChange=false;
                        txtnoOfDays.setFocusable(true);
//                        Days = txtnoOfDays.getText().toString();
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
            }
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2) {
            case "simvalidated" : {
                if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
                    showToast("Please Login again");
                    Intent intent = new Intent(ActivateSim.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    showToast(errorMessage);
                    txtnoOfDays.getText().clear();
                    edtSerialNumber.getText().clear();
                    ignoreChange=false;
                }
            }
        }
    }

//    public void btnValidateSIM1Click( View view ) {
//        //edtSerialNumber = findViewById(R.id.editSerialNo);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            showToast("Sorry! Something went wrong");
//                            return;
//                        }
//
//                         Serial = edtSerialNumber.getText().toString();
//

//                        authenticationPresenter.validateSim(Serial,token);
//
//                    }
//                });
//
//    }

    public void btnBuyNowClick(View view) {
        try {
            if (edtSerialNumber.getText().toString().isEmpty()) {
                throw new Exception("Please Enter SIM Serial Number");
            }
            if (txtnoOfDays.getText().toString().equals("0") || txtnoOfDays.getText().toString().isEmpty()) {
                throw new Exception("Please Enter Valid Number Of Days");
            } else {
                Days = txtnoOfDays.getText().toString();
                Serial = edtSerialNumber.getText().toString();
//                newActivationRequest = new NewActivationRequest();
//                newActivationRequest.setNumberOfDays(Days);
//                newActivationRequest.setSerialNumber(Serial);
//                newActivationRequest.setAmountCharged(String.valueOf(Amount));
//                newActivationRequest.setRequestedForDtTm(DateToday.toString());
//                newActivationRequest.setRefNo("1");
//                newActivationRequest.setRequestedDevice("2");
//                newActivationRequest.setRequestedIP("24");
//                newActivationRequest.setRequestedOS("android");


                Intent PaymentSummary = new Intent(ActivateSim.this, mPayment.class);
                PaymentSummary.putExtra("SerialNumber", Serial);
                PaymentSummary.putExtra("NumberOfDays", Days);
                PaymentSummary.putExtra("RequestedIP", "24");
                PaymentSummary.putExtra("RequestedOS", "Android");
                PaymentSummary.putExtra("AmountCharged", String.valueOf(Amount));
                PaymentSummary.putExtra("RequestedForDtTm", DateToday.toString());
                PaymentSummary.putExtra("RefNo", "1");
                PaymentSummary.putExtra("RequestedDevice", "2");
                startActivity(PaymentSummary);
                finish();
            }
        } catch (Exception e) {
            String[] Error = e.toString().split(":");
            showToast(Error[1]);
        }
    }



//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//
//        try {
//            if (!hasFocus) {
//                if (edtSerialNumber.getText().toString().length() == 20) {
//
//                    Serial = edtSerialNumber.getText().toString();
//                    if (Serial != null && !Serial.isEmpty()) {
//
//                        FirebaseInstanceId.getInstance().getInstanceId()
//                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                        if (!task.isSuccessful()) {
//                                            showToast("Sorry! Something went wrong");
//                                            return;
//                                        }
//                                        authenticationPresenter.validateSim(Serial, token);
//
//                                    }
//                                });
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            String[] Error = e.toString().split(":");
//            showToast(Error[1]);
//        }
//
//    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

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

    public void getCurretDatePicker(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(ActivateSim.this,R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        todayDate.setText(year + "-" +(monthOfYear + 1  ) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }

    public void calenderButton(View view){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(ActivateSim.this,R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        todayDate.setText(year + "-" +(monthOfYear + 1  ) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis());
        picker.show();
    }
}
