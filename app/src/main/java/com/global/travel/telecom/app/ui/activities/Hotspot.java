package com.global.travel.telecom.app.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Hotspot {
    static WifiManager.LocalOnlyHotspotReservation mReservation;

    public void hotspotFxn(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_hotspot);
        dialog.show();
        final WifiManager wifi_manager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);

        TextView textOK = dialog.findViewById(R.id.textOk);
        TextView text1 = dialog.findViewById(R.id.text1);


        textOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    configApState(context);
                    dialog.dismiss();

                } catch (Exception e) {
                    Toast.makeText(context, "catch " + e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });
    }

    // here is the hotspot function code
    // this code is run in android versio 5.1 // click to on and again click to off
    //write the code her for anoother version hotspot code
    //ui already created for both activity (on and off)

    public static boolean configApState(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {

            // if WiFi is on, turn it off
            if (isApOn(context)) {
                Toast.makeText(context, "wifi on ", Toast.LENGTH_LONG).show();
                wifimanager.setWifiEnabled(false);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, !isApOn(context));
            Toast.makeText(context, "on hotspot ", Toast.LENGTH_LONG).show();

            return true;
        } catch (Exception e) {
            Toast.makeText(context, "catch 2 " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return false;
    }

    //check whether wifi hotspot on or off
    public static boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        } catch (Throwable ignored) {

        }
        return false;
    }

    private void turnOffHotspot() {

        if (mReservation != null) {
            mReservation.close();
//            isHotspotEnabled = false;
        }
    }
}

