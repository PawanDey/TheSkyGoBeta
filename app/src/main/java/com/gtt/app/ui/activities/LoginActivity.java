package com.gtt.app.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
import com.gtt.app.R;
import com.gtt.app.base.BaseActivity;
import com.gtt.app.model.LoginRequestTypeId;
import com.gtt.app.model.LoginResponse;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {
    ImageView fb;
    LoginButton LoginButton;
    Button GooglesignInButton;
    GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    AuthenticationPresenter authenticationPresenter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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

        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        callbackManager = CallbackManager.Factory.create();
        fb = findViewById(R.id.facebookCustom);

        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();

        authenticationPresenter = new AuthenticationPresenter(this);


    }

    @Override
    public void onFailure() {
        showToast("Sorry! Something went wrong");

    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "loginUser": {
                showToast("Login Successful");
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
                Log.d("mylog", "Successful: " + loginResult.getAccessToken().toString());
                Log.d("mylog", "User ID: " + Profile.getCurrentProfile().getId());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().getApplicationId());
                Log.d("mylog", "EMAIL2;" + loginResult.getAccessToken().getToken());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().getUserId());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().describeContents());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().getLastRefresh());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().getDataAccessExpirationTime());
                Log.d("mylog", "EMAIL;" + loginResult.getAccessToken().isExpired());


                Log.d("mylog", "User Profile Pic Link: " + Profile.getCurrentProfile().getProfilePictureUri(500, 500));
                Toast toast = Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT);
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    showToast("Sorry! Something went wrong");
                                    return;
                                }
                                String token = task.getResult().getToken();
                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    showToast("Sorry! Something went wrong");
                                                    return;
                                                }
                                                String token = task.getResult().getToken();
                                                authenticationPresenter.loginUser(loginResult.getAccessToken().getToken(), LoginRequestTypeId.GOOGLE, token);

                                            }
                                        });

                            }
                        });
            }

            @Override
            public void onCancel() {
                // App code
                Toast toast = Toast.makeText(getApplicationContext(), "Login cancel", Toast.LENGTH_SHORT);

            }

            @Override
            public void onError(FacebookException exception) {
                // App code0
                Log.d("mylog", "Successful: " + exception.toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Login cancel error", Toast.LENGTH_SHORT);

            }
        });
    }


    public void GmailLoginButton(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

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
            Log.d("aaaaaaaaaa", "EMAIL;" + account.getEmail());
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                showToast("Sorry! Something went wrong");
                                return;
                            }
                            String token = task.getResult().getToken();

                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                showToast("Sorry! Something went wrong");
                                                return;
                                            }
                                            String token = task.getResult().getToken();
                                            authenticationPresenter.loginUser(account.getEmail(), LoginRequestTypeId.GOOGLE, token);

                                        }
                                    });

                        }
                    });
        } catch (ApiException e) {

            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
}
