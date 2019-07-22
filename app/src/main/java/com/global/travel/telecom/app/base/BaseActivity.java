package com.global.travel.telecom.app.base;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseView;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected abstract int getLayout();

    private ProgressBar progressBar;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            this.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
//        }
//        if (Build.VERSION.SDK_INT >= 21) {
////            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }



        setContentView(getLayout());
        onViewReady();
    }

    protected void onViewReady() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        if( progressDialog == null ){
            View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loader, null);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder( this ).setView(mDialogView);
            progressDialog = mBuilder.create();
            progressDialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT) );
            progressDialog.getWindow().setLayout( WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT  );
        }
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        progressDialog.dismiss();
    }

    private void setWindowFlag(int bits,Boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags = winParams.flags | bits;
        } else {
            winParams.flags = winParams.flags & Integer.reverse(bits);
        }
        win.setAttributes( winParams );
    }

}
