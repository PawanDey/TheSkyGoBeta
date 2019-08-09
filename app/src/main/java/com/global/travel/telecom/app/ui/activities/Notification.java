package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.GetNotifications;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Notification extends BaseActivity {

    imageGalleryAdapterNotification adapter;
    ImageGalleryAdapterNotificationEN adapter2;
    public BaseView baseView;
    RecyclerView recyclerView;
    String mName, mMsg, mTime;
    int num = 0;
    ArrayList<String> translatePaasData = new ArrayList<String>();
    List<translatPassdata> list = new ArrayList<>();


    @Override
    protected int getLayout() {

        return R.layout.activity_notification;
    }

    protected void onViewReady() {
        super.onViewReady();

        AuthenticationPresenter authenticationPresenter = new AuthenticationPresenter(this);
        UserDetails userDetails = new UserDetails(this);
        authenticationPresenter.ListNotifications(userDetails.getTokenID(), "1");

    }

    public void backToDashboardButton(View view) {
        finish();

    }

    private WifiManager.LocalOnlyHotspotReservation mReservation;
    Context context = this;

    public void hotspotButton(View view) {
        Hotspot hotspot=new Hotspot();
        hotspot.hotspotFxn(context);
    }

    @Override
    public void onFailure() {
        Toast.makeText(Notification.this, R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
    }

    //        @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {

            case "notification": {
                GetNotifications getNotifications = new GetNotifications();
                UserDetails userDetails = new UserDetails(this);

                userDetails.getLanguageSelect();

                if (userDetails.getLanguageSelect().equals("en")) {
                    List<GetNotifications> result = (List<GetNotifications>) response;
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    adapter2 = new ImageGalleryAdapterNotificationEN(result, getApplication());
                    recyclerView.setAdapter(adapter2);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));
                } else {
                    List<GetNotifications> result = (List<GetNotifications>) response;
//                    mName = result.get(0).mDealerName;
//                    for (int i = 0; i < result.size(); i++) {
//                        translatePaasData.add(result.get(i).mMessage);
//                        translatePaasData.add(result.get(i).mAlertTime);
//                    }
//                    for (int j = 0; j < translatePaasData.size(); j++) {
                    TranslateaApiCall(result);
//                        try {
//                            Thread.sleep(600);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
                break;
            }

            case "translateAPI": {
                GetNotifications getNotifications = new GetNotifications();
                UserDetails userDetails = new UserDetails(this);

                if (num == 0) {
                    mMsg = response.toString();
                    num = 1;
                } else {
                    mTime = response.toString();
                    num = 0;

//                String translateDone[] = response.toString().split("");

                    list.add(new translatPassdata(mName, mMsg, mTime));

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    adapter = new imageGalleryAdapterNotification(list, getApplication());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));

                }
            }


        }

    }

    private void TranslateaApiCall(List<GetNotifications> translatePaasData) {
        AuthenticationPresenter authenticationPresenter = new AuthenticationPresenter(this);
        UserDetails userDetails = new UserDetails(this);
        authenticationPresenter.TranslateAPI("en", userDetails.getLanguageSelect(), translatePaasData);


    }


    @Override
    public void onServerError(String method, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


}
