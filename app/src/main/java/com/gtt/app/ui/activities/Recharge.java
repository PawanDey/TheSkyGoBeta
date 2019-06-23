package com.gtt.app.ui.activities;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.text.format.DateFormat;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

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
        import java.util.Date;

public class Recharge extends BaseActivity {

    AuthenticationPresenter authenticationPresenter;
    EditText edtTextMSISDN;
    TextView txtnoOfDaysRecharge;
    TextView totalAmountRecharge;
    EditText numberOfDaysRecharge;
    String Days = "0";
    double rate = 0.0;
    double Amount =0.0;
    public BaseView baseView;
    TextView todayDate;
    String token;
    String MSISDN;
    Button MSISDNRecharge;
    Date d =new Date();
    TextView GoodUntil;
    CharSequence todayDateRecharge  = DateFormat.format("MMMM d, yyyy ", d.getTime());

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
        UserDetails userDetails = new UserDetails(Recharge.this);
        todayDate.setText(todayDateRecharge);
        token=userDetails.getTokenID();
        edtTextMSISDN = findViewById(R.id.edtMSISDN);
        edtTextMSISDN.setText(userDetails.getMSISDN());
        MSISDNRecharge = (Button)findViewById(R.id.MSISDNRecharge);
        MSISDNRecharge.performClick();
        numberOfDaysRecharge = findViewById(R.id.txtnumberOfDaysRecharge);
        numberOfDaysRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Days = numberOfDaysRecharge.getText().toString();
                try {
                    Amount = ((Integer.parseInt(Days)) * rate);
                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    String TotalAmount = formatter.format(Amount);
                    totalAmountRecharge.setText("$" + TotalAmount);
                }
                catch (Exception e)
                {
                    if (edtTextMSISDN.getText().toString().isEmpty())
                    {
                        showToast("Please Enter SIM Serial Number");
                    }
                    else
                        showToast("Invalid Number Of Day");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        numberOfDaysRecharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Days = numberOfDaysRecharge.getText().toString();
//                try {
//                    Amount = ((Integer.parseInt(Days)) * rate);
//                    NumberFormat formatter = NumberFormat.getNumberInstance();
//                    formatter.setMinimumFractionDigits(2);
//                    formatter.setMaximumFractionDigits(2);
//                    String TotalAmount = formatter.format(Amount);
//                    totalAmountRecharge.setText("$" + TotalAmount);
//                }
//                catch (Exception e)
//                {
//                    if (edtTextMSISDN.getText().toString().isEmpty())
//                    {
//                        showToast("Please Enter SIM Serial Number");
//                    }
//                    else
//                        showToast("Invalid Number Of Day");
//                }
//            }
//        });

    }
    @Override
    public void onFailure() {
        showToast("Sorry! Something went wrong");
    }

    @Override
    public void onSuccess(String method3, Object response) {
        switch (method3){
            case "rechargeMSISDN" : {
                ActivateSimResponse obj = (ActivateSimResponse) response;
                GoodUntil.setText(obj.getmLastValidityDate());
                rate = Integer.parseInt(obj.getmRatePerDay());
                

//                totalAmountRecharge = (TextView)findViewById(R.id.totalAmountActivate);
//                try {
//                    Days = Integer.parseInt(numberOfDaysRecharge.getText().toString());
//                    rate = Float.parseFloat(obj.getmRatePerDay());
//                    Amount = (Days*rate);
//                    lastValidDate = (TextView) findViewById(R.id.validDateLeftRC);
//                    lastValidDate.setText(obj.getmLastValidityDate());
//                    totalAmountRecharge.setText(Double.toString(Amount));
//                    showToast(obj.getResponseMessage());
//                }
//                catch (Exception e)
//                {
//                    txtnoOfDaysRecharge.setText("0");
//                    totalAmountRecharge.setText("0.00");
//                    showToast(e.toString());
//                }
            }
        }
    }

    @Override
    public void onServerError(String method2, String errorMessage) {
        switch (method2){
            case "rechargeMSISDN" : {
                showToast( errorMessage );
            }
        }
    }

    public void btnValidateMSISDNClick( View view ) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            showToast("Sorry! Something went wrong");
                            return;
                        }
                        Bundle extras = getIntent().getExtras();
                        MSISDN = edtTextMSISDN.getText().toString();
                        authenticationPresenter.validateMSISDN(MSISDN,token);

                    }
                });

    }
    public void btnBuyNowClick(View view)
    {
        try {
            if (edtTextMSISDN.getText().toString().isEmpty()) {
                throw new Exception("Please Enter SIM Serial Number");
            }
            if (txtnoOfDaysRecharge.getText().toString().equals("0") || txtnoOfDaysRecharge.getText().toString().isEmpty()) {
                throw new Exception("Please Enter Valid Number Of Days");
            } else {
                Days = txtnoOfDaysRecharge.getText().toString();
                MSISDN = edtTextMSISDN.getText().toString();
//                NewExtensionRequest newExtensionRequest = new NewExtensionRequest();
//                newExtensionRequest.setNumberOfDays(Days);
//                newExtensionRequest.setMSISDN(MSISDN);
//                newExtensionRequest.setAmountCharged(String.valueOf(Amount));
//                newExtensionRequest.setRequestedForDtTm(todayDateRecharge.toString());
////                newActivationRequest.setToken(token);
//                newExtensionRequest.setRefNo("1");
//                newExtensionRequest.setRequestedDevice("2");
//                newExtensionRequest.setRequestedIP("24");
//                newExtensionRequest.setRequestedOS("android");
//                //authenticationPresenter.activateSim(newActivationRequest);


                Intent paymnetSummaryR = new Intent(Recharge.this, mPayment.class);
                paymnetSummaryR.putExtra("MSISDN", MSISDN);
                paymnetSummaryR.putExtra("NumberOfDaysR", Days);
                paymnetSummaryR.putExtra("RequestedIPR", "24");
                paymnetSummaryR.putExtra("RequestedOSR", "Android");
                paymnetSummaryR.putExtra("AmountChargedR", String.valueOf(Amount));
                paymnetSummaryR.putExtra("RequestedForDtTmR", todayDateRecharge.toString());
                paymnetSummaryR.putExtra("RefNoR", "1");
                paymnetSummaryR.putExtra("RequestedDeviceR", "2");
                startActivity(paymnetSummaryR);
                finish();
            }
        } catch (Exception e) {
            String[] Error = e.toString().split(":");
            showToast(Error[1]);
        }

    }

    public void backToDashboardButton(View view){
        finish();
    }
    public  void notificatioButton(View view){
        Intent i = new Intent(this,Notification.class);
        startActivity(i);
            }

}
