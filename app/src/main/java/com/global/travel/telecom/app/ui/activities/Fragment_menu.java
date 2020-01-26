package com.global.travel.telecom.app.ui.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Fragment_menu extends Fragment {
    private TextView menu_name, voipNumber, currentBalance;
    private Activity activity;
    private LinearLayout menu_linearLayoutMain;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Handler mHandler1 = new Handler(Looper.getMainLooper());
    private ListView listViewVoipPlan;
    private String userBalance = SkyGoDialer.userBalance;
    public ArrayList<VoipPlanModel> VoipPlan = SkyGoDialer.VoipPlan;
    ProgressBar progressBar;
    DatePickerDialog picker;
    String timeFormat = "d MMMM,yyyy";
    androidx.appcompat.app.AlertDialog progressDialog;
    DatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        menu_name = view.findViewById(R.id.menu_name);
        voipNumber = view.findViewById(R.id.voipNumber);
        currentBalance = view.findViewById(R.id.currentBalance);
        listViewVoipPlan = view.findViewById(R.id.menu_listView);
        menu_linearLayoutMain = view.findViewById(R.id.menu_linearLayoutMain);
        progressBar = view.findViewById(R.id.menu_progressBar);

        try {
            if (userBalance.equals("")) {
                menu_linearLayoutMain.setVisibility(View.GONE);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (userBalance.equals("")) {
                                userBalance = SkyGoDialer.userBalance;
                            }
                            menu_linearLayoutMain.setVisibility(View.VISIBLE);
                            currentBalance.setText("$" + userBalance);
                            menu_name.setText("SkyGo:" + SkyGoDialer.userID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            } else {
                menu_linearLayoutMain.setVisibility(View.VISIBLE);
                currentBalance.setText("$" + userBalance);
                menu_name.setText("SkyGo:" + SkyGoDialer.userID);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "fragment_menu_userbalanced_error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            if (VoipPlan == null) {
                mHandler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        while (VoipPlan == null) {
                            try {
                                VoipPlan = SkyGoDialer.VoipPlan;
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        VoipPlanArrayAdapter adapter = new VoipPlanArrayAdapter(getApplicationContext(), R.layout.menu_voip_plans, VoipPlan);
                        listViewVoipPlan.setAdapter(adapter);
                        listViewVoipPlan.setVisibility(View.VISIBLE);
                    }
                }, 4000);

            } else {
                progressBar.setVisibility(View.GONE);
                VoipPlanArrayAdapter adapter = new VoipPlanArrayAdapter(getApplicationContext(), R.layout.menu_voip_plans, VoipPlan);
                listViewVoipPlan.setAdapter(adapter);
                listViewVoipPlan.setBackgroundColor(getResources().getColor(R.color.white));
                listViewVoipPlan.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Voip_Select_Plan_Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        listViewVoipPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                View contactCarePopUp = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_popup, null);
                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext()).setView(contactCarePopUp);
                progressDialog = mBuilder.create();
                Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                progressDialog.show();

                //ID
                TextView cancle = contactCarePopUp.findViewById(R.id.menu_Cancle);
                TextView OK = contactCarePopUp.findViewById(R.id.menu_OK);
                datePicker = (DatePicker) contactCarePopUp.findViewById(R.id.simpleDatePicker);
                Calendar c = Calendar.getInstance();
                datePicker.setMinDate(System.currentTimeMillis());
                long time = System.currentTimeMillis();
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth() + 1;
                        int year = datePicker.getYear();

                        VoipPlanModel arrayList = (VoipPlanModel) listViewVoipPlan.getItemAtPosition(position);
                        String getPlanName = arrayList.getPlanName();
                        String getMonikerValue = arrayList.getMonikerValue();
                        String getValidity = arrayList.getValidity();
                        String getPlanMin = arrayList.getPlanMin();
                        String getPlanDetails = arrayList.getPlanDetails();
                        String getAmountCharge = arrayList.getAmountCharge();

                        arrayList.getMonikerValue();
                        Intent PaymentSummary = new Intent(getContext(), mPayment.class);
                        PaymentSummary.putExtra("Number", getPlanName);
                        PaymentSummary.putExtra("NumberOfDays", getValidity);
                        PaymentSummary.putExtra("AmountCharged", getAmountCharge);
                        PaymentSummary.putExtra("RequestedForDtTm", year + "-" + month + "-" + day + " 00:00:00Z");
                        PaymentSummary.putExtra("AppPaymentType", "3");
                        PaymentSummary.putExtra("MonikerValue", getMonikerValue);
                        PaymentSummary.putExtra("PlanMin", getPlanMin);
                        startActivity(PaymentSummary);
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

}
