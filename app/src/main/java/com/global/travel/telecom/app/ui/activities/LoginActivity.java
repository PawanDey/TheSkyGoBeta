package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.widget.Toast.LENGTH_LONG;


public class LoginActivity extends BaseActivity {
    ImageView fb, google, linkedin, ownEmail;
    ProgressBar processBar;
    EditText input_email_signin, input_password_signin, confrom_input_password_signin, input_login_email, input_login_password;
    TextView text_below_signIn, text_below_login, verificationText;
    LinearLayout SignInPageLayout, LogInPageLayout;
    RelativeLayout verificationLayout;
    Button createaccount_signIn, Button_login;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    AuthenticationPresenter authenticationPresenter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    androidx.appcompat.app.AlertDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected int getLayout() {

        //app permisson request
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int phone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
//        int contact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int microphone = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

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
//        if (contact != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
//        }
        if (microphone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
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
        try {
            firebaseAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            showToast("Firebase Auth Error");
        }
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        fb = findViewById(R.id.facebookCustom);
        google = findViewById(R.id.google_sign_in_button);
        linkedin = findViewById(R.id.linkedin_button);
        ownEmail = findViewById(R.id.ownemail_sign_in_button);
        authenticationPresenter = new AuthenticationPresenter(this);
        SelectLoginImage();
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "loginUser": {
                Toast.makeText(LoginActivity.this, R.string.textLoginSuccessful, LENGTH_LONG).show();
                LoginResponse obj = (LoginResponse) response;
                UserDetails userDetails = new UserDetails(LoginActivity.this);
                userDetails.setTokenID(obj.getTokenID());
                userDetails.setUserName(obj.getUserName());
                userDetails.setPaypalTransactionFee(obj.getTxnSeriesPrefix());
                userDetails.setTxnSeriesPrefix("SKY");
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                intent.putExtra("TokenID", userDetails.getTokenID());
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onServerError(String method, String errorMessage) {
        if ("loginUser".equals(method)) {
            showToast(errorMessage);
        }
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, LanguageSelect.class);
        startActivity(i);
        finish();
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

                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT);
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String getUserName;
                        if (!task.isSuccessful()) {
//                            showToast("Sorry! Something went wrong");
                            Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
                            return;
                        }
                        String token = Objects.requireNonNull(task.getResult()).getToken();
                        if (profile != null) {
                            getUserName = profile.getId();
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
                Toast.makeText(LoginActivity.this, R.string.textLogincancel, LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(LoginActivity.this, R.string.textNOInternetConnection, LENGTH_LONG).show();
                } else
                    Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
            }

        });
    }

    public void GmailLoginButton(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);
//        authenticationPresenter.loginUser("ssssssss@gmail.com", LoginRequestTypeId.GOOGLE, "2");

    }

    public void ownEmailLoginButton(View view) {
        View mViewSiginScreen = LayoutInflater.from(this).inflate(R.layout.own_email_signin_screen, null);
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(mViewSiginScreen);
        progressDialog = mBuilder.create();
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        progressDialog.show();

        // ID value
        try {

            //layout id
            SignInPageLayout = mViewSiginScreen.findViewById(R.id.SignInPageLayout);
            LogInPageLayout = mViewSiginScreen.findViewById(R.id.LogInPageLayout);
            processBar = mViewSiginScreen.findViewById(R.id.processBar);
            verificationLayout = mViewSiginScreen.findViewById(R.id.verificationLayout);

            //signin
            input_email_signin = mViewSiginScreen.findViewById(R.id.input_email_signin);
            input_password_signin = mViewSiginScreen.findViewById(R.id.input_password_signin);
            confrom_input_password_signin = mViewSiginScreen.findViewById(R.id.confrom_input_password_signin);
            text_below_signIn = mViewSiginScreen.findViewById(R.id.text_below_signIn);
            createaccount_signIn = mViewSiginScreen.findViewById(R.id.createaccount_signIn);
            verificationText = mViewSiginScreen.findViewById(R.id.verificationText);

            //login
            input_login_email = mViewSiginScreen.findViewById(R.id.input_email_loginIn);
            input_login_password = mViewSiginScreen.findViewById(R.id.input_password_loginIn);
            Button_login = mViewSiginScreen.findViewById(R.id.Button_login);
            text_below_login = mViewSiginScreen.findViewById(R.id.text_below_login);

            Button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (input_login_email.getText().toString().isEmpty() || input_login_password.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "E-mail/Password is Empty", LENGTH_LONG).show();
                        return;
                    }
                    processBar.setVisibility(View.VISIBLE);
                    user = firebaseAuth.getCurrentUser();

                    firebaseAuth.signInWithEmailAndPassword(input_login_email.getText().toString().trim(), input_login_password.getText().toString().trim()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (user != null) {
                                    if (task.isSuccessful()) {
                                        if (!user.isEmailVerified()) {
                                            Toast.makeText(getApplicationContext(), " Your Email is not Verified ", Toast.LENGTH_SHORT).show();
                                        } else if (user.isEmailVerified()) {
                                            //go to dashboard activity
                                            FirebaseInstanceId.getInstance().getInstanceId()
                                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                            if (!task.isSuccessful()) {
                                                                Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
                                                                return;
                                                            }
                                                            String token = Objects.requireNonNull(task.getResult()).getToken();
                                                            authenticationPresenter.loginUser(Objects.requireNonNull(user.getEmail()).trim(), LoginRequestTypeId.Email, token);
                                                        }
                                                    });


                                            Toast.makeText(getApplicationContext(), " Login Succesful: Verified " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), " Connection Error! Please Try Again", Toast.LENGTH_SHORT).show();

                                        }
                                    } else if (task.isComplete()) {
                                        Toast.makeText(getApplicationContext(), " Email/password is invalid", Toast.LENGTH_SHORT).show();
                                    } else if (task.isCanceled()) {
                                        Toast.makeText(getApplicationContext(), " Authentication Cancle ", Toast.LENGTH_SHORT).show();
                                    }
                                    processBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Connection Error! Please Try Again", LENGTH_LONG).show();
                                    processBar.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), LENGTH_LONG).show();
                                processBar.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            createaccount_signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (input_email_signin.getText().toString().isEmpty() || input_password_signin.getText().toString().isEmpty() || confrom_input_password_signin.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fill All details ", LENGTH_LONG).show();
                        return;
                    }
                    if (input_password_signin.length() < 5 || confrom_input_password_signin.length() < 5) {
                        Toast.makeText(getApplicationContext(), "Password too short", LENGTH_LONG).show();
                        return;
                    }
                    if (!input_password_signin.getText().toString().equals(confrom_input_password_signin.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password Not match", LENGTH_LONG).show();
                        return;
                    }

                    processBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(input_email_signin.getText().toString().trim(), input_password_signin.getText().toString().trim())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    verificationLayout.setVisibility(View.VISIBLE);
                                                    verificationText.setText("A verification link has been send to " + input_email_signin.getText().toString().trim());
                                                    Toast.makeText(getApplicationContext(), "A verification link has been send to " + input_email_signin.getText().toString().trim(), LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), LENGTH_LONG).show();

                                    }
                                    processBar.setVisibility(View.GONE);
                                }
                            });
                }
            });

            text_below_signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SignInPageLayout.setVisibility(View.GONE);
                    LogInPageLayout.setVisibility(View.VISIBLE);

                }
            });
            text_below_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogInPageLayout.setVisibility(View.GONE);
                    SignInPageLayout.setVisibility(View.VISIBLE);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;
            account.getEmail();
            Log.d("Email", "EMAIL;" + account.getEmail());
            Log.d("name", "NAME;" + account.getDisplayName());

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, LENGTH_LONG).show();
                                return;
                            }
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            authenticationPresenter.loginUser(account.getEmail(), LoginRequestTypeId.GOOGLE, token);
                        }
                    });
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, R.string.textLogincancel, LENGTH_LONG).show();
        }
//            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(LoginActivity.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();

    }

    private void check() {
        LocationManager lm = (LocationManager) LoginActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
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

        switch (languageCode) {
            case "en":

                break;
            //che
            case "zh":
                try {
                    fb.setImageResource(R.drawable.facebook_che);
                    google.setImageResource(R.drawable.google_che);
                    ownEmail.setImageResource(R.drawable.emaillogin_che);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "ja":
                try {
                    fb.setImageResource(R.drawable.facebook_jap);
                    google.setImageResource(R.drawable.google_jap);
                    ownEmail.setImageResource(R.drawable.emaillogin_jap);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            //kor
            case "ko":
                try {
                    fb.setImageResource(R.drawable.facebook_kor);
                    google.setImageResource(R.drawable.google_kor);
                    ownEmail.setImageResource(R.drawable.emaillogin_kor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "es":
                try {
                    fb.setImageResource(R.drawable.facebook_esp);
                    google.setImageResource(R.drawable.google_esp);
                    ownEmail.setImageResource(R.drawable.emaillogin_esp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                showToast("no language select");

                break;
        }
    }

    public void backToExit(View view) {
        finish();
    }
}
