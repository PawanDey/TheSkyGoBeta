package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.service.UserDetails;

import java.util.Locale;

public class LanguageSelect extends BaseActivity {
    ImageView english, chinese, japanese, korean,spanish;
    TextView next;

    @Override
    protected int getLayout() {
        return R.layout.activity_language_select;
    }


    @Override
    protected void onViewReady() {
        super.onViewReady();
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(String method, Object response) {

    }

    @Override
    public void onServerError(String method, String errorMessage) {

    }

    public void SelectEnglish(View view) {
        callMethod("en");
    }

    public void SelectChinese(View view) {
        callMethod("zh");

    }

    public void SelectJapanese(View view) {
        callMethod("ja");
    }

    public void SelectKorean(View view) {
        callMethod("ko");

    }
    public void SelectSPANISH(View view) {
        callMethod("es");
    }

    public void backToExit(View view) {
        finish();
    }

    public void callMethod(String Locate) {
        Locale locale = new Locale(Locate);
        UserDetails userDetails = new UserDetails(this);
        userDetails.setLanguageSelect(locale.toString());
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        Intent LangActivity = new Intent(this, LoginActivity.class);
        startActivity(LangActivity);
        finish();
    }

}
