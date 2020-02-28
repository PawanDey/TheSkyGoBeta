package com.global.travel.telecom.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.global.travel.telecom.app.R;

public class PaymentSucessfull extends AppCompatActivity {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sucessfull);
        bundle = getIntent().getExtras();
        String screenType = bundle.getString("screenType");
        TextView msg = findViewById(R.id.msgShowInPaymentSuccessfullScreen);
        if (screenType.equals("1")) {

        } else if (screenType.equals("2")) {

        } else if (screenType.equals("3")) {

        } else if (screenType.equals("4")) {
            msg.setText(bundle.getString("msg"));
        }
    }

    public void backToHome(View view) {
        Intent DashboardIntent = new Intent(this, Dashboard.class);
        DashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(DashboardIntent);
        finish();
    }
}
