package com.global.travel.telecom.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.global.travel.telecom.app.R;

public class PaymentSucessfull extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sucessfull);
    }

    public void backToHome(View view) {
        Intent DashboardIntent = new Intent(this, Dashboard.class);
        DashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(DashboardIntent);
        finish();
    }
}
