package com.gtt.app.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gtt.app.R;
import com.gtt.app.base.BaseActivity;
import com.gtt.app.model.GetNotifications;
import com.gtt.app.presenter.implementation.AuthenticationPresenter;
import com.gtt.app.service.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Notification extends BaseActivity {

    imageGalleryAdapterNotification adapter;
    RecyclerView recyclerView;


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

    public void hotspotButton(View view) {

    }

    @Override
    public void onFailure() {
        showToast("Sorry! Something went wrong");
    }

    @Override
    public void onSuccess(String method, Object response) {
        List<GetNotifications> result = (List<GetNotifications>) response;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new imageGalleryAdapterNotification(result, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Notification.this));


    }


    @Override
    public void onServerError(String method, String errorMessage) {
        showToast(errorMessage);
    }
}
