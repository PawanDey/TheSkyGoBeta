package com.global.travel.telecom.app.base;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.global.travel.telecom.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected abstract int getLayout();

    private AlertDialog progressDialog;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private androidx.appcompat.app.AlertDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToFirebaseToCheckMaintenaceStatus();
        setContentView(getLayout());
        onViewReady();
    }

    protected void onViewReady() {
    }

    @Override
    public void showToast(String message) {
        mHandler.postDelayed(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(), 500);
    }

    @Override
    public void showProgressBar() {
        if (progressDialog == null) {
            @SuppressLint("InflateParams") View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loader, null);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this).setView(mDialogView);
            progressDialog = mBuilder.create();
            try {
                progressDialog.setCancelable(false);
            } catch (Exception e) {
                progressDialog.setCanceledOnTouchOutside(false);
            }
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
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
                    showToast(Objects.requireNonNull(a.get("under_maintenance_message")).toString());
//                    showUnderMaintenanceDialog(Objects.requireNonNull(a.get("under_maintenance_message")).toString());
                } else {
                    dismissUnderMaintenanceDialog();
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
