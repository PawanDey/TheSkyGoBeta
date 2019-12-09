package com.global.travel.telecom.app.base;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.drawable.ColorDrawable;

import com.global.travel.telecom.app.R;

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
           try {
               progressDialog.setCancelable(false);
           }catch (Exception e){
               progressDialog.setCanceledOnTouchOutside(false);
           }
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
