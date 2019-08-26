package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.MediaRouteButton;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends BaseActivity {
    ImageView fb, google, linkedin;
    LoginButton LoginButton;
    Button GooglesignInButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    AuthenticationPresenter authenticationPresenter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public BaseView baseView;

    @Override
    protected int getLayout() {

        //app permisson request
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int phone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }

        check();


        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        callbackManager = CallbackManager.Factory.create();

        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        fb = findViewById(R.id.facebookCustom);
        google = findViewById(R.id.google_sign_in_button);
        linkedin = findViewById(R.id.linkedin_button);
        authenticationPresenter = new AuthenticationPresenter(this);
        SelectLoginImage();

    }

    @Override
    public void onFailure() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(LoginActivity.this, R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "loginUser": {
                Toast.makeText(LoginActivity.this, R.string.textLoginSuccessful, Toast.LENGTH_LONG).show();
                LoginResponse obj = (LoginResponse) response;
                UserDetails userDetails = new UserDetails(LoginActivity.this);
                userDetails.setTokenID(obj.getTokenID());
                userDetails.setUserName(obj.getUserName());
                userDetails.setPaypalTransactionFee(obj.getTxnSeriesPrefix());
                userDetails.setTxnSeriesPrefix(obj.getTxnSeriesPrefix());
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                intent.putExtra("TokenID", userDetails.getTokenID());
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onServerError(String method, String errorMessage) {
        switch (method) {
            case "loginUser": {
                showToast(errorMessage);
            }
        }
    }


    public void FacebookLoginButtton(View view) {
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.performClick();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                final Profile profile = Profile.getCurrentProfile();
                Log.d("mylog", "Successful: " + loginResult.getAccessToken().toString());
                Log.d("mylog", "getApplicationId;" + loginResult.getAccessToken().getApplicationId());
                Log.d("mylog", "getToken;" + loginResult.getAccessToken().getToken());
                Log.d("mylog", "getUserId;" + loginResult.getAccessToken().getUserId());
                Log.d("mylog", "describeContents;" + loginResult.getAccessToken().describeContents());
                Log.d("mylog", "getLastRefresh;" + loginResult.getAccessToken().getLastRefresh());
                Log.d("mylog", "getDataAccessExpirationTime;" + loginResult.getAccessToken().getDataAccessExpirationTime());
                Log.d("mylog", "isExpired;" + loginResult.getAccessToken().isExpired());

                try {
                    Log.d("mylog", "getName;" + profile.getName());
                    Log.d("mylog", "getId;" + profile.getId());
                    Log.d("mylog", "getProfilePictureUri;" + profile.getProfilePictureUri(500, 500));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast toast = Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT);
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String getUserName;
                        if (!task.isSuccessful()) {
//                            showToast("Sorry! Something went wrong");
                            Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                            return;
                        }
                        String token = task.getResult().getToken();
                        if (profile != null) {
                            getUserName = profile.getName();
                        } else {
                            getUserName = loginResult.getAccessToken().getUserId();
//                            getUserName= "user";

                        }
                        authenticationPresenter.loginUser(getUserName, LoginRequestTypeId.FACEBOOK, token);
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.textLogincancel, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(LoginActivity.this, R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
            }

        });
    }


    public void GmailLoginButton(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
//        authenticationPresenter.loginUser("ssssssss@gmail.com", LoginRequestTypeId.GOOGLE, "2");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            account.getEmail();
            Log.d("Email", "EMAIL;" + account.getEmail());
            Log.d("name", "NAME;" + account.getDisplayName());

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
                                return;
                            }
                            String token = task.getResult().getToken();
                            authenticationPresenter.loginUser(account.getDisplayName(), LoginRequestTypeId.GOOGLE, token);
                        }
                    });
        } catch (ApiException e) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            Toast.makeText(LoginActivity.this, R.string.textLogincancel, Toast.LENGTH_LONG).show();
        }
//            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, LanguageSelect.class);
        startActivity(i);
        finish();
    }

    private void check() {
        LocationManager lm = (LocationManager) LoginActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
//            dialog.setMessage("Gps is not enabled");
            dialog.setCancelable(false);
            dialog.setMessage(R.string.textGPSisnotenabled);
//            dialog.setPositiveButton("Open Setting", new DialogInterface.OnClickListener()
            dialog.setPositiveButton(R.string.textOpensettings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    LoginActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
//            dialog.setNegativeButton("if you cancel your app will be closed", new DialogInterface.OnClickListener() {
            dialog.setNegativeButton(R.string.textifyoucancelyourappwillbeclosed, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //this is for close the app
//                    finishAffinity();


                }
            });
            dialog.show();
        }
    }

    public void SelectLoginImage() {
        UserDetails userDetails = new UserDetails(this);
        try {
            Locale locale = new Locale(userDetails.getLanguageSelect());
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String languageCode = userDetails.getLanguageSelect();
        //eng
        if (languageCode.equals("en")) {

        }
        //che
        else if (languageCode.equals("zh")) {
            try {
                fb.setImageResource(R.drawable.facebook_che);
                google.setImageResource(R.drawable.google_che);
                linkedin.setImageResource(R.drawable.linkedin_che);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (languageCode.equals("ja")) {
            try {
                fb.setImageResource(R.drawable.facebook_jap);
                google.setImageResource(R.drawable.google_jap);
                linkedin.setImageResource(R.drawable.linkedin_jap);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        //kor
        else if (languageCode.equals("ko")) {
            try {
                fb.setImageResource(R.drawable.facebook_kor);
                google.setImageResource(R.drawable.google_kor);
                linkedin.setImageResource(R.drawable.linkedin_kor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (languageCode.equals("es")) {
            try {
                fb.setImageResource(R.drawable.facebook_esp);
                google.setImageResource(R.drawable.google_esp);
                linkedin.setImageResource(R.drawable.linkedin_esp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast(" no language select");

        }
    }
}
