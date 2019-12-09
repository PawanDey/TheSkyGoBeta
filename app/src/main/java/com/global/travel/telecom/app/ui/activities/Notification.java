package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.GetNotifications;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.util.ArrayList;
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
        try {
            Hotspot hotspot = new Hotspot();
            hotspot.hotspotFxn(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), R.string.textNOInternetConnection, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.textSorrySomethingwentwrong, Toast.LENGTH_LONG).show();
        }
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
                    mName = result.get(0).mDealerName;
                    for (int i = 0; i < result.size(); i++) {
                        translatePaasData.add(result.get(i).mMessage);
                        translatePaasData.add(result.get(i).mAlertTime);
                    }
                    for (int j = 0; j < translatePaasData.size(); j++) {
                        try {
                            TranslateaApiCall(translatePaasData.get(j));
                            Thread.sleep(650);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
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

    private void TranslateaApiCall(String translatePaasData) {
        AuthenticationPresenter authenticationPresenter = new AuthenticationPresenter(this);
        UserDetails userDetails = new UserDetails(this);
        authenticationPresenter.TranslateAPI("en", userDetails.getLanguageSelect(), translatePaasData);


    }


    @Override
    public void onServerError(String method, String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


}
