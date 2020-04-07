package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.GetUserProfileDate;
import com.global.travel.telecom.app.model.PostUpdateUserProfileData;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class UserProfile extends BaseActivity {
    Context context = this;
    private EditText userProfile_name, userProfile_email, userProfile_number;
    private CountryCodePicker ccp;
    private AuthenticationPresenter authenticationPresenter;
    UserDetails userDetails;
    String getUserCountryName = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDetails = new UserDetails(this);
        authenticationPresenter = new AuthenticationPresenter(this);
        ccp = findViewById(R.id.ccp);
        userProfile_name = findViewById(R.id.userProfile_name);
        userProfile_email = findViewById(R.id.userProfile_email);
        userProfile_number = findViewById(R.id.userProfile_number);

        String appLanguage = userDetails.getLanguageSelect();
        switch (appLanguage) {
            case "es":
                ccp.changeLanguage(CountryCodePicker.Language.SPANISH);
                break;
            case "ja":
                ccp.changeLanguage(CountryCodePicker.Language.JAPANESE);
                break;
            case "zh":
                ccp.changeLanguage(CountryCodePicker.Language.CHINESE);
                break;
        }
        try {
            if (!userDetails.getTokenID().isEmpty()) {
                authenticationPresenter.GetUserProfileData(userDetails.getTokenID());
            }
        } catch (Exception e) {
            hideProgressBar();
            e.printStackTrace();
        }
        ccp.setOnCountryChangeListener(() -> {
            getUserCountryName = ccp.getSelectedCountryNameCode();
        });
    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "GetUserProfileData": {
                GetUserProfileDate getUserProfileDate = (GetUserProfileDate) response;
                userProfile_name.setText(getUserProfileDate.getName());
                userProfile_email.setText(getUserProfileDate.getEmailID());
                userProfile_number.setText(getUserProfileDate.getContactPerson());
                String country = getUserProfileDate.getAddress();

                if (!country.equals("")) {
                    ccp.setCountryForNameCode(country);
                    getUserCountryName = country;
                } else {
                    ccp.setCountryForNameCode("US");
                    getUserCountryName = "US";
                }
                break;
            }
            case "UpdateUserProfileData": {
                showToast("Profile updated.");
                break;
            }
        }
        hideProgressBar();
    }

    @Override
    public void onServerError(String method, String errorMessage) {
        if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
            Toast.makeText(UserProfile.this, getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserProfile.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            showToast(errorMessage);
        }
        hideProgressBar();
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textNOInternetConnection), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
        }
        hideProgressBar();
    }

    public void UpdateProfile(View view) {
        if (userProfile_name.getText().toString().equals("") ||
                userProfile_number.getText().toString().equals("") ||
                userProfile_email.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.textFillAlldetails));
        } else {
            PostUpdateUserProfileData request = new PostUpdateUserProfileData();
            request.setTokenID(userDetails.getTokenID());
            request.setName(userProfile_name.getText().toString().trim());
            request.setEmailId(userProfile_email.getText().toString().trim());
            request.setHomeCountry(getUserCountryName);
            request.setMobile(userProfile_number.getText().toString().trim());
            authenticationPresenter.UpdateUserProfileData(request);
        }

    }
}
