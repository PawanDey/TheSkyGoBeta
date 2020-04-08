package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.TransactionDetailsActivationExtentionVoIPModel;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TransactionDetails extends BaseActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<TransactionDetailsActivationExtentionVoIPModel>> listDataChild;
    AuthenticationPresenter authenticationPresenter;
    UserDetails userDetails;
    List<TransactionDetailsActivationExtentionVoIPModel> ae;
    List<TransactionDetailsActivationExtentionVoIPModel> voip;

    @Override
    protected int getLayout() {
        return R.layout.activity_transaction_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expListView = findViewById(R.id.ExpandableListView);
        authenticationPresenter = new AuthenticationPresenter(this);
        userDetails = new UserDetails(this);
        try {
            authenticationPresenter.GetAllTransaction(userDetails.getTokenID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "GetAllTransactionActivationExtension": {
                List<TransactionDetailsActivationExtentionVoIPModel> transactionDetailsActivationExtentionVoIPModel = (List<TransactionDetailsActivationExtentionVoIPModel>) response;
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();

                // Adding child data
                listDataHeader.add("Activation / Extenstion");
                listDataHeader.add("VoIP");

                // Adding child data
                ae = transactionDetailsActivationExtentionVoIPModel;
                listDataChild.put(listDataHeader.get(0), ae);


                break;
            }
            case "GetAllTransactionVoIP": {

                List<TransactionDetailsActivationExtentionVoIPModel> transactionDetailsActivationExtentionVoIPModel = (List<TransactionDetailsActivationExtentionVoIPModel>) response;
                voip = transactionDetailsActivationExtentionVoIPModel;
                listDataChild.put(listDataHeader.get(1), voip);
                listAdapter = new TransactionDetails_ExpandableListAdapter(this, listDataHeader, listDataChild);
                // setting list adapter
                expListView.setAdapter(listAdapter);
                hideProgressBar();
                break;
            }
        }
    }

    @Override
    public void onServerError(String method, String errorMessage) {
        if (errorMessage.contains("Token Authentication Failed") || errorMessage.contains("User Authentication Failed")) {
            Toast.makeText(this, getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            showToast(errorMessage);
        }
        hideProgressBar();
    }

    @Override
    public void onFailure() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textNOInternetConnection), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.textSorrySomethingwentwrong), Toast.LENGTH_LONG).show();
        }
        hideProgressBar();
    }

}
