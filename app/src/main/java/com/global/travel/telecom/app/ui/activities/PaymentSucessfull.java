package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.global.travel.telecom.app.R;

public class PaymentSucessfull extends AppCompatActivity {
    Bundle bundle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sucessfull);
        bundle = getIntent().getExtras();
        TextView msgShowInPaymentSuccessfullScreen = findViewById(R.id.msgShowInPaymentSuccessfullScreen);
        TextView msgorderNumber = findViewById(R.id.msgorderNumber);
        TextView msgpayment = findViewById(R.id.msgpayment);
        msgShowInPaymentSuccessfullScreen.setText(bundle.getString("msg"));
        msgorderNumber.setText(getResources().getString(R.string.textOrderNumber) + " " + bundle.getString("orderNumber"));
        msgpayment.setText(getResources().getString(R.string.textPayment) + ": $" + bundle.getDouble("payment"));
    }

    public void backToHome(View view) {
        Intent DashboardIntent = new Intent(this, Dashboard.class);
        DashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(DashboardIntent);
        finish();
    }

    public void reviewYoutOrder(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://theskygo.com/"));
        startActivity(browserIntent);
    }
}
