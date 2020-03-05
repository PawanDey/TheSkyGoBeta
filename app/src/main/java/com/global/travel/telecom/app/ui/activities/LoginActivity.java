package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import com.global.travel.telecom.app.model.CreateVoipCustomerSkyGo;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberError;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberGood;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_LONG;


public class LoginActivity extends BaseActivity {
    ImageView fb, google, linkedin, ownEmail;
    ProgressBar processBar, login_ff_processBar;
    EditText input_email_signin, input_password_signin, confrom_input_password_signin, input_login_email, input_login_password, inputOTP, login_ff_phonenumber;
    TextView text_below_signIn, text_below_login, verificationText;
    CountryCodePicker ccp;
    LinearLayout SignInPageLayout, LogInPageLayout;
    RelativeLayout verificationLayout;
    Button createaccount_signIn, Button_login, sendOTP, login_ff_log_in;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    AuthenticationPresenter authenticationPresenter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    androidx.appcompat.app.AlertDialog progressDialog;
    androidx.appcompat.app.AlertDialog progressDialog1;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    LoginResponse obj;
    UserDetails userDetails;
    CreateVoipCustomerSkyGo createVoipCustomerSkyGo = new CreateVoipCustomerSkyGo();
    String getUserCountryCode, getUserCountryName, parareqTypeID, ParaUsername, paraGcmKey, paraName, paraEmailID, paraPhoneNumber;
    String facebookGetName = "";
    String mVerificationId = "";
    String countryCodeValue = "";

    @Override
    protected int getLayout() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int phone = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        int contact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int microphone = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
////        if (storage != PackageManager.PERMISSION_GRANTED) {
////            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
////        }
        if (phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (contact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (microphone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;
        countryCodeValue = tm.getNetworkCountryIso();
        check();
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
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
        userDetails = new UserDetails(this);
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textNOInternetConnection), LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textSorrySomethingwentwrong), LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "loginUser": {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.textLoginSuccessful), LENGTH_LONG).show();
                obj = (LoginResponse) response;
                UserDetails userDetails = new UserDetails(LoginActivity.this);
                userDetails.setTokenID(obj.getTokenID());
                userDetails.setUserId(obj.getUserID());
                userDetails.setUserName(obj.getUserName());
                userDetails.setPaypalTransactionFee(obj.getTxnSeriesPrefix());
                userDetails.setTxnSeriesPrefix("SKY");
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                intent.putExtra("TokenID", userDetails.getTokenID());
                startActivity(intent);
                finish();
                break;
            }
            case "CreateVoipAccount": {
                try {
                    obj = (LoginResponse) response;

                    String VoIpName = "SkyGo:" + obj.getUserID().trim();
                    //set in share prefence
                    userDetails.setUserId(obj.getUserID());
                    userDetails.setVoipCredentailuserName(obj.getVoIPUserName());
                    userDetails.setVoipCredentailPassword(obj.getVoIPPassword());
                    userDetails.setVoipUserName(VoIpName);

                    //set data for create a customer in skygo database
                    createVoipCustomerSkyGo.setmTokenID(obj.getTokenID());
                    createVoipCustomerSkyGo.setmVoIPId_SIPId("");
                    createVoipCustomerSkyGo.setmAmount(obj.getVoIPCreditLimit());
                    createVoipCustomerSkyGo.setmCLINumber("");
                    createVoipCustomerSkyGo.setmVoip_name(VoIpName);
                    createVoipCustomerSkyGo.setmCustomer_reference(obj.getUserID().trim());
                    createVoipCustomerSkyGo.setmSubscriber_reference(obj.getUserID());
                    createVoipCustomerSkyGo.setmEmail_address("");
                    createVoipCustomerSkyGo.setmContact_number("");
                    createVoipCustomerSkyGo.setmEmail_address("");
                    createVoipCustomerSkyGo.setmAddress_line_2("");
                    createVoipCustomerSkyGo.setmAddress_line_1("");
                    createVoipCustomerSkyGo.setmPostcode("");
                    createVoipCustomerSkyGo.setmCountry("USA");
                    createVoipCustomerSkyGo.setmFirst_name("");
                    createVoipCustomerSkyGo.setmMiddle_initials("");
                    createVoipCustomerSkyGo.setmSurname("SkyGo");
                    createVoipCustomerSkyGo.setmTitle("Mr");
                    //3 paramters (subscriber_id , customer_id and voip_status) change value if success
                    createVoipCustomerSkyGo.setmCustomer_id("0");
                    createVoipCustomerSkyGo.setmSubscriber_id("0");
                    createVoipCustomerSkyGo.setmVoip_status("30");

                    String createCustomerAndSubscriber = "<create-customer-and-subscriber version=\"1\">\n" +
                            "<authentication>\n" +
                            "<username>" + obj.getVoIPUserName().trim() + "</username>\n" +
                            "<password>" + obj.getVoIPPassword().trim() + "</password>\n" +
                            "</authentication>\n" +
                            "<customer>\n" +
                            "<name>" + VoIpName + "</name>\n" +
                            "<customer-reference>" + obj.getUserID().trim() + "</customer-reference>\n" +
                            "<distributor-id>" + obj.getVoIPDistributorId().trim() + "</distributor-id>\n" +
                            "<status>active</status>\n" +
                            "<credit-basis>" + obj.getVoIPCreditBasis().trim() + "</credit-basis>\n" +
                            "<credit-limit>" + obj.getVoIPCreditLimit().trim() + "</credit-limit>\n" +
                            "<warning-trigger>" + obj.getVoIPWarningTrigger().trim() + "</warning-trigger>\n" +
                            "<customer-group>" + obj.getVoIPCustomerGroup().trim() + "</customer-group>\n" +
                            "<email-address></email-address>\n" +
                            "<contact-number></contact-number>\n" +
                            "<address-line-1></address-line-1>\n" +
                            "<address-line-2></address-line-2>\n" +
                            "<address-line-3></address-line-3>\n" +
                            "<address-line-4></address-line-4>\n" +
                            "<postcode></postcode>\n" +
                            "<country>USA</country>\n" +
                            "</customer>\n" +
                            "<subscriber>\n" +
                            "<first-name></first-name>\n" +
                            "<middle-initials></middle-initials>\n" +
                            "<surname>SkyGo</surname>\n" +
                            "<title>Mr</title>\n" +
                            "<status>active</status>\n" +
                            "<enable-sip-registrations>" + obj.getVoIPEnableSipRegistration().trim() + "</enable-sip-registrations>\n" +
                            "<prefer-sip>" + obj.getVoIPPreferSip().trim() + "</prefer-sip>\n" +
                            "<voicemail-enabled>" + obj.getVoIPVoicemailEnable().trim() + "</voicemail-enabled>\n" +
                            "<voicemail-timeout>" + obj.getVoIPVoicemailTimeout().trim() + "</voicemail-timeout>\n" +
                            "<notify-missed-calls>" + obj.getVoIPNotifyMissedCall().trim() + "</notify-missed-calls>\n" +
                            "<send-charge-notifications>" + obj.getVoIPSendChargeNotifications().trim() + "</send-charge-notifications>\n" +
                            "<send-credit-notifications>" + obj.getVoIPSendCreditNotifications().trim() + "</send-credit-notifications>\n" +
                            "<forward-to>" + obj.getVoIPForwardTo().trim() + "</forward-to>\n" +
                            "<withhold-cli>" + obj.getVoIPWithholdCLI().trim() + "</withhold-cli>\n" +
                            "<email-address></email-address>\n" +
                            "<subscriber-reference>" + obj.getUserID() + "</subscriber-reference>\n" +
                            "<forward-callback>" + obj.getVoIPForwardCallback().trim() + "</forward-callback>\n" +
                            "<auto-cli>" + obj.getVoIPAutoCLI().trim() + "</auto-cli>\n" +
                            "<block-gprs>" + obj.getVoIPBlockGPRS().trim() + "</block-gprs>\n" +
                            "</subscriber>\n" +
                            "</create-customer-and-subscriber>";

                    authenticationPresenter.VoIPAPICall(createCustomerAndSubscriber, "createCustomerAndSubscriber");
                } catch (Exception e) {
                    showToast("OnSuccess CreateVoipAccount Error:" + e.getMessage());
                    e.printStackTrace();
                }
                break;
            }
            case "CreateCustomerAndSubscriberError": {
                try {
                    VoipCreateCustomerAndSubscriberError obj = (VoipCreateCustomerAndSubscriberError) response;
                    showToast("VoiP CreateCustomerAndSubscriberError: " + obj.getCreateCustomerAndSubscriberError().getContent());

                    authenticationPresenter.CreateVoipCustomerSkyGo(createVoipCustomerSkyGo);

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("onSuccess CreateCustomerAndSubscriberError Error: " + e.getMessage());
                }
                break;
            }
            case "CreateCustomerAndSubscriberGood": {
                try {
                    VoipCreateCustomerAndSubscriberGood obj = (VoipCreateCustomerAndSubscriberGood) response;

                    //add pendnig 3 parametre for create voip customer skygo (in case success)
                    createVoipCustomerSkyGo.setmCustomer_id(obj.getCreateCustomerAndSubscriberResponse().getCustomer().getId());
                    createVoipCustomerSkyGo.setmSubscriber_id(obj.getCreateCustomerAndSubscriberResponse().getSubscriber().getId());
                    createVoipCustomerSkyGo.setmVoip_status("31");

                    //set in share prefnce
                    userDetails.setVoipCustomerID(obj.getCreateCustomerAndSubscriberResponse().getCustomer().getId());
                    userDetails.setVoipSubcriberID(obj.getCreateCustomerAndSubscriberResponse().getSubscriber().getId());

                    String setSubscriberPassword = "<set-subscriber-password version=\"1\"> <authentication>\n" +
                            "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                            "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password> </authentication>\n" +
                            "<subscriberid>" + userDetails.getVoipSubcriberID().trim() + "</subscriberid>\n" +
                            "<username>" + userDetails.getVoipUserName().trim() + "</username> \n" +
                            "<password>" + userDetails.getUserId().trim() + "</password>\n" +
                            "</set-subscriber-password>";

                    //for set subscriber password
                    authenticationPresenter.VoIPAPICall(setSubscriberPassword, "setSubscriberPassword");

                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("on Success CreateCustomerAndSubscriberGood Error: " + e.getMessage());
                }
                break;
            }
            case "setSubscriberPasswordGood":
            case "setSubscriberPasswordError": {
                authenticationPresenter.CreateVoipCustomerSkyGo(createVoipCustomerSkyGo);
                break;
            }
            case "CreateVoipCustomerSkyGo": {
                showToast(getResources().getString(R.string.textVoipAccountCreated));
                break;
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
                if (profile != null) {
                    Log.d("mylog", "getName;" + profile.getName());
                    Log.d("mylog", "getId;" + profile.getId());
                    Log.d("mylog", "getProfilePictureUri;" + profile.getProfilePictureUri(500, 500));
                    facebookGetName = profile.getName();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.textLogin), Toast.LENGTH_SHORT);
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    String getUserName;
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.textSorrySomethingwentwrong), LENGTH_LONG).show();
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    if (profile != null) {
                        getUserName = profile.getId();
                    } else {
                        getUserName = loginResult.getAccessToken().getUserId();
                    }
                    LoginFillFormFuction(getUserName, facebookGetName, "33", token, "", "", "");
//                    authenticationPresenter.loginUser(getUserName, LoginRequestTypeId.FACEBOOK, token);
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.textLogincancel), LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.textNOInternetConnection), LENGTH_LONG).show();
                } else
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.textSorrySomethingwentwrong), LENGTH_LONG).show();
            }

        });
    }

    public void GmailLoginButton(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 0);

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

            Button_login.setOnClickListener(v -> {
                if (input_login_email.getText().toString().isEmpty() || input_login_password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textEmailPasswordIsEmpty), LENGTH_LONG).show();
                    return;
                }
                processBar.setVisibility(View.VISIBLE);
                user = firebaseAuth.getCurrentUser();

                firebaseAuth.signInWithEmailAndPassword(input_login_email.getText().toString().trim(), input_login_password.getText().toString().trim()).addOnCompleteListener(LoginActivity.this, task -> {
                    try {
                        if (user != null) {
                            if (task.isSuccessful()) {
                                if (!user.isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textYourEmailisNotVerified), Toast.LENGTH_SHORT).show();
                                } else if (user.isEmailVerified()) {
                                    //go to dashboard activity
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnCompleteListener(task1 -> {
                                                if (!task1.isSuccessful()) {
                                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.textSorrySomethingwentwrong), LENGTH_LONG).show();
                                                    return;
                                                }
                                                String token = Objects.requireNonNull(task1.getResult()).getToken();
                                                LoginFillFormFuction(user.getEmail(), "", "35", token, user.getEmail().trim(), "", "");
//                                                authenticationPresenter.loginUser(Objects.requireNonNull(user.getEmail()).trim(), LoginRequestTypeId.Email, token);
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textConnectionErrorPleaseTryAgain), Toast.LENGTH_SHORT).show();

                                }
                            } else if (task.isComplete()) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.textEmailpasswordisInvalid), Toast.LENGTH_SHORT).show();
                            } else if (task.isCanceled()) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.textAuthenticationCancle), Toast.LENGTH_SHORT).show();
                            }
                            processBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textConnectionErrorPleaseTryAgain), LENGTH_LONG).show();
                            processBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), LENGTH_LONG).show();
                        processBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });

            });

            createaccount_signIn.setOnClickListener(v -> {

                if (input_email_signin.getText().toString().isEmpty() || input_password_signin.getText().toString().isEmpty() || confrom_input_password_signin.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textFillAlldetails), LENGTH_LONG).show();
                    return;
                }
                if (input_password_signin.length() < 5 || confrom_input_password_signin.length() < 5) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textPasswordTooShort), LENGTH_LONG).show();
                    return;
                }
                if (!input_password_signin.getText().toString().equals(confrom_input_password_signin.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textPasswordNotmatch), LENGTH_LONG).show();
                    return;
                }

                processBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(input_email_signin.getText().toString().trim(), input_password_signin.getText().toString().trim())
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(LoginActivity.this, task12 -> {
                                    if (task12.isSuccessful()) {
                                        verificationLayout.setVisibility(View.VISIBLE);
                                        verificationText.setText(getResources().getString(R.string.textAVerificationLinkHasBeenSendTo) + " " + input_email_signin.getText().toString().trim());
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.textAVerificationLinkHasBeenSendTo) + " " + input_email_signin.getText().toString().trim(), LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), LENGTH_LONG).show();
                            }
                            processBar.setVisibility(View.GONE);
                        });
            });

            text_below_signIn.setOnClickListener(v -> {

                SignInPageLayout.setVisibility(View.GONE);
                LogInPageLayout.setVisibility(View.VISIBLE);

            });
            text_below_login.setOnClickListener(v -> {

                LogInPageLayout.setVisibility(View.GONE);
                SignInPageLayout.setVisibility(View.VISIBLE);
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
            account.getId();
            Log.d("Email", "EMAIL;" + account.getEmail());
            Log.d("name", "NAME;" + account.getDisplayName());

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.textSorrySomethingwentwrong), LENGTH_LONG).show();
                            return;
                        }
                        String token = Objects.requireNonNull(task.getResult()).getToken();
                        LoginFillFormFuction(account.getEmail(), account.getDisplayName(), "34", token, account.getEmail(), "", "");
//                        authenticationPresenter.loginUser(account.getEmail(), LoginRequestTypeId.GOOGLE, token);

                    });
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.textLogincancel), LENGTH_LONG).show();
        }
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
            dialog.setMessage(getResources().getString(R.string.textGPSisnotenabled));
            dialog.setPositiveButton(getResources().getString(R.string.textOpensettings), (paramDialogInterface, paramInt) -> {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                LoginActivity.this.startActivity(myIntent);
                //get gps
            });
            dialog.setNegativeButton(getResources().getString(R.string.textifyoucancelyourappwillbeclosed), (paramDialogInterface, paramInt) -> {
                //this is for close the app
//                    finishAffinity();
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

    public void LoginFillFormFuction(String username, String name, String reqTypeID, String gcmKey, String email, String mobileNumner, String homeCountry) {
        View loginfillForm = LayoutInflater.from(this).inflate(R.layout.login_fill_dataform, null);
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(loginfillForm);
        progressDialog1 = mBuilder.create();
        Objects.requireNonNull(progressDialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog1.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        progressDialog1.show();

        //find IDS
        login_ff_processBar = loginfillForm.findViewById(R.id.login_ff_processBar);
        EditText login_ff_name = loginfillForm.findViewById(R.id.login_ff_username);
        EditText login_ff_emailid = loginfillForm.findViewById(R.id.login_ff_emailid);
        login_ff_phonenumber = loginfillForm.findViewById(R.id.login_ff_phonenumber);
        inputOTP = loginfillForm.findViewById(R.id.inputOTP);
        ccp = loginfillForm.findViewById(R.id.ccp);
        sendOTP = loginfillForm.findViewById(R.id.sendOTP);
        login_ff_log_in = loginfillForm.findViewById(R.id.login_ff_log_in);
        if (!countryCodeValue.equals("")) {
            ccp.setCountryForNameCode(countryCodeValue);
            ccp.setDefaultCountryUsingNameCode(countryCodeValue);
        } else {
            ccp.setCountryForNameCode("US");
            ccp.setDefaultCountryUsingNameCode("US");
        }
        // ARABIC, BENGALI, CHINESE, ENGLISH, FRENCH, GERMAN, GUJARATI, HINDI, JAPANESE, JAVANESE, PORTUGUESE, RUSSIAN, SPANISH
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

        getUserCountryCode = ccp.getDefaultCountryCodeWithPlus();
        getUserCountryName = ccp.getDefaultCountryNameCode();

        login_ff_name.setText(name);
        login_ff_emailid.setText(email);
        if (!email.isEmpty()) {
            login_ff_emailid.setEnabled(false);
        }
        if (!name.isEmpty()) {
            login_ff_name.setEnabled(false);
        }
        ccp.setOnCountryChangeListener(() -> {
            getUserCountryCode = ccp.getSelectedCountryCodeWithPlus();
            getUserCountryName = ccp.getSelectedCountryNameCode();
        });

        sendOTP.setOnClickListener(v -> {
            if (login_ff_phonenumber.getText().length() < 1) {
                showToast(getResources().getString(R.string.textEnterNumberValidNumber));
                return;
            }
            inputOTP.setVisibility(View.VISIBLE);
            sendOTP.setVisibility(View.GONE);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    getUserCountryCode + login_ff_phonenumber.getText().toString().trim(),
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);

        });

        login_ff_log_in.setOnClickListener(v -> {
            if (login_ff_name.getText().toString().isEmpty() || login_ff_emailid.getText().toString().isEmpty() || login_ff_phonenumber.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.textFillAlldetails), LENGTH_LONG).show();
                return;
            }
            parareqTypeID = reqTypeID;
            ParaUsername = username;
            paraGcmKey = gcmKey;
            paraName = login_ff_name.getText().toString().trim();
            paraEmailID = login_ff_emailid.getText().toString().trim();
            paraPhoneNumber = getUserCountryCode + login_ff_phonenumber.getText().toString().trim();
            if (inputOTP.getText().length() == 6) {
                login_ff_processBar.setVisibility(View.VISIBLE);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, inputOTP.getText().toString().trim());
                signInWithPhoneAuthCredential(credential);
            } else {
                showToast(getResources().getString(R.string.textPleaseEnter6DigitsOTP));
            }
        });

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d("", "onVerificationCompleted:" + phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            showToast(e.getMessage());
            inputOTP.setVisibility(View.GONE);
            sendOTP.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d("", "onCodeSent:" + verificationId);
            mVerificationId = verificationId;
            showToast(getResources().getString(R.string.textVerificationOTPsendSuccesfully));
            login_ff_log_in.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            //code for timeout and show the resend passcode
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        authenticationPresenter.loginUser(paraName, paraEmailID, paraPhoneNumber, getUserCountryName, parareqTypeID, ParaUsername, paraGcmKey);
                        progressDialog1.dismiss();
                    } else {
                        Log.w("", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            showToast(getResources().getString(R.string.textInvalidCode));
                            login_ff_processBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
