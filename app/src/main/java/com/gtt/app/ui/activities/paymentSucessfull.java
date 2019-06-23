package com.gtt.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gtt.app.R;

public class paymentSucessfull extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_sucessfull);


    }
    public void backToHome (View view){
        Intent i= new Intent(this,Dashboard.class);
        startActivity(i);
        finish();
    }
}
