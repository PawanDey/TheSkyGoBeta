package com.global.travel.telecom.app.ui.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.service.UserDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import io.fabric.sdk.android.Fabric;

import static com.crashlytics.android.Crashlytics.log;

public class SplashScreen extends AppCompatActivity {

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    public AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    ImageView image;
    TextView versionName;
    private androidx.appcompat.app.AlertDialog progressDialog1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        image = findViewById(R.id.imageView4);
        setContentView(R.layout.activity_splash_screen);
        checkForAppUpdate();
        try {
            versionName = findViewById(R.id.versionCode);
            versionName.setText(getResources().getString(R.string.textVersion) + ":" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void moveAnimation() {
        Animation img = new TranslateAnimation(Animation.ABSOLUTE, 150, Animation.ABSOLUTE, Animation.ABSOLUTE);
        img.setDuration(2800);
        img.setFillAfter(true);
        image.startAnimation(img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkNewAppVersionState();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQ_CODE_VERSION_UPDATE) {
            if (resultCode != RESULT_OK) {
                log("Update flow failed! Result code: " + resultCode);
                unregisterInstallStateUpdListener();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }


    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED)
                popupSnackbarForCompleteUpdateAndUnregister();
        };

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        startAppUpdateImmediate(appUpdateInfo);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                connectToFirebaseToCheckMaintenaceStatus();
            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) throws IntentSender.SendIntentException {
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, SplashScreen.REQ_CODE_VERSION_UPDATE);
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    SplashScreen.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "An update has just been downloaded.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Res", v -> appUpdateManager.completeUpdate());
        snackbar.show();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(
                appUpdateInfo -> {
                    //FLEXIBLE:
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdateAndUnregister();
                    }

                    //IMMEDIATE:
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            startAppUpdateImmediate(appUpdateInfo);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }

    }

    private void connectToFirebaseToCheckMaintenaceStatus() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap a = (HashMap) dataSnapshot.getValue();
                assert a != null;
                if (Boolean.parseBoolean(Objects.requireNonNull(a.get("is_under_maintenance")).toString())) {
                    showUnderMaintenanceDialog(Objects.requireNonNull(a.get("under_maintenance_message")).toString());
                } else {
                    dismissUnderMaintenanceDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        UserDetails userDetails = new UserDetails(SplashScreen.this);
                        Locale locale = new Locale(userDetails.getLanguageSelect());
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        if (!userDetails.getTokenID().equals("")) {
                            Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashScreen.this, LanguageSelect.class);
                            startActivity(intent);
                        }
                    }, 3000);
                    image = findViewById(R.id.imageView4);
                    moveAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

    @SuppressLint("ResourceType")
    private void showUnderMaintenanceDialog(String underMaintenanceMessage) {
        @SuppressLint("InflateParams") View um = LayoutInflater.from(this).inflate(R.layout.under_maintenance_popup, null);
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(this).setView(um);
        progressDialog1 = mBuilder.create();
        progressDialog1.setCancelable(false);
        Objects.requireNonNull(progressDialog1.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        progressDialog1.show();
        TextView maintenanceMassage = um.findViewById(R.id.maintenanceMassage);
        maintenanceMassage.setText(underMaintenanceMessage);
    }

    private void dismissUnderMaintenanceDialog() {
        if (progressDialog1 != null && progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }
    }
}