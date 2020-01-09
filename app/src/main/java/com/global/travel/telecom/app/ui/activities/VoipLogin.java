package com.global.travel.telecom.app.ui.activities;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;

public class VoipLogin extends BaseActivity {
    AuthenticationPresenter authenticationPresenter;
    TextView textView13, textView14, textView15;

    @Override
    protected int getLayout() {
        return R.layout.activity_voip_login;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        textView13 = findViewById(R.id.textView13);
        textView14 = findViewById(R.id.textView19);
        textView15 = findViewById(R.id.textView20);

        authenticationPresenter = new AuthenticationPresenter(this);
        String data = "<get-customer-balance version=\"1\"><authentication><username>skygo.sumit</username><password>54321</password></authentication><number>447624090652</number></get-customer-balance>";
        try {
            authenticationPresenter.GetCurrentBalance(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "CurrentBalance": {

                final CurrentBalance currentBalance = (CurrentBalance) response;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        textView13.setText(currentBalance.getGetCustomerBalanceResponse().getBalance().getContent());
                        textView14.setText(currentBalance.getGetCustomerBalanceResponse().getTrid().toString());
                        textView15.setText(currentBalance.getGetCustomerBalanceResponse().getClearedBalance().getContent());
                    }
                });

                break;
            }
            case "": {

                break;
            }
        }
    }

    @Override
    public void onServerError(String method, String errorMessage) {

    }
}
