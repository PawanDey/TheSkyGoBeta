package com.global.travel.telecom.app.ui.activities;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.VoipPlanModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.mCountry_wise_rateList;

public class Fragment_menu extends Fragment {
    private TextView menu_name, voipNumber, currentBalance, countryWiseRateDiscription;
    private LinearLayout menu_linearLayoutMain;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ListView listViewVoipPlan;
    public ArrayList<VoipPlanModel> VoipPlan = SkyGoDialer.VoipPlan;
    ProgressBar progressBar;
    Spinner snipper_country;
    DatePickerDialog picker;
    String timeFormat = "d MMMM,yyyy";
    androidx.appcompat.app.AlertDialog progressDialog;
    DatePicker datePicker;
    public Country_wise_price_adapter mCountry_wise_price_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        menu_name = view.findViewById(R.id.menu_name);
        voipNumber = view.findViewById(R.id.voipNumber);
        currentBalance = view.findViewById(R.id.currentBalance);
        listViewVoipPlan = view.findViewById(R.id.menu_listView);
        menu_linearLayoutMain = view.findViewById(R.id.menu_linearLayoutMain);
        progressBar = view.findViewById(R.id.menu_progressBar);
        countryWiseRateDiscription = view.findViewById(R.id.countryWiseRateDiscription);
        snipper_country = view.findViewById(R.id.snipper_country_wise_rate);
        try {
            if (SkyGoDialer.userBalance.equals("")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (SkyGoDialer.userBalance.equals("")) {
                                Thread.sleep(200);
                            }
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    menu_linearLayoutMain.setVisibility(View.VISIBLE);
                                    currentBalance.setText("$" + SkyGoDialer.userBalance);
                                    menu_name.setText("SkyGo:" + SkyGoDialer.userID);
                                }
                            }, 100);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } else {
                menu_linearLayoutMain.setVisibility(View.VISIBLE);
                currentBalance.setText("$" + SkyGoDialer.userBalance);
                menu_name.setText("SkyGo:" + SkyGoDialer.userID);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "fragment_menu_userbalanced_error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            if (VoipPlan == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (VoipPlan == null) {
                            try {
                                VoipPlan = SkyGoDialer.VoipPlan;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                VoipPlanArrayAdapter adapter = new VoipPlanArrayAdapter(getApplicationContext(), R.layout.menu_voip_plans, VoipPlan);
                                listViewVoipPlan.setAdapter(adapter);
                                listViewVoipPlan.setVisibility(View.VISIBLE);
                            }
                        }, 100);

                    }
                }).start();


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

        try {
            mCountry_wise_price_adapter = new Country_wise_price_adapter(getContext(), mCountry_wise_rateList);
            snipper_country.setAdapter(mCountry_wise_price_adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        listViewVoipPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    View contactCarePopUp = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_popup, null);
                    androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext()).setView(contactCarePopUp);
                    progressDialog = mBuilder.create();
                    Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    progressDialog.show();

                    //ID
                    TextView cancle = contactCarePopUp.findViewById(R.id.menu_Cancle);
                    TextView OK = contactCarePopUp.findViewById(R.id.menu_OK);
                    datePicker = contactCarePopUp.findViewById(R.id.simpleDatePicker);
                    Date date = new Date();
                    datePicker.setMinDate(date.getTime());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        snipper_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country_wise_rate_list co = (Country_wise_rate_list) parent.getItemAtPosition(position);
                countryWiseRateDiscription.setText(co.getmPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

}
