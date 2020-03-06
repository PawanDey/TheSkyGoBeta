package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.GetVoipPlanModel;
import com.global.travel.telecom.app.model.GetVoipRateModel;
import com.global.travel.telecom.app.service.UserDetails;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.global.travel.telecom.app.ui.activities.SkyGoDialer.mCountry_wise_rateList;

public class Fragment_menu extends Fragment {
    private TextView menu_name, countryWiseRateDiscription, addBalancedFunction, checkCountryWisePrice;
    @SuppressLint("StaticFieldLeak")
    static TextView currentBalanceMenu;
    private LinearLayout menu_linearLayoutMain;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ListView listViewVoipPlan;
    private ArrayList<GetVoipPlanModel> VoipPlan = SkyGoDialer.VoipPlan;
    private ProgressBar progressBar;
    private Spinner snipper_country;
    private androidx.appcompat.app.AlertDialog progressDialog;
    private DatePicker datePicker;
    UserDetails userDetails = new UserDetails(getApplicationContext());
    private String amount = "0";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Eastern"));
        menu_name = view.findViewById(R.id.menu_name);
        currentBalanceMenu = view.findViewById(R.id.currentBalance);
        listViewVoipPlan = view.findViewById(R.id.menu_listView);
        menu_linearLayoutMain = view.findViewById(R.id.menu_linearLayoutMain);
        progressBar = view.findViewById(R.id.menu_progressBar);
        checkCountryWisePrice = view.findViewById(R.id.checkCountryWisePrice);
        countryWiseRateDiscription = view.findViewById(R.id.countryWiseRateDiscription);
        snipper_country = view.findViewById(R.id.snipper_country_wise_rate);
        addBalancedFunction = view.findViewById(R.id.addBalancedFunction);

        try {
            if (SkyGoDialer.userBalance.equals("")) {
                new Thread(() -> {
                    try {
                        while (SkyGoDialer.userBalance.equals("")) {
                            Thread.sleep(200);
                        }
                        mHandler.postDelayed(() -> {
                            menu_linearLayoutMain.setVisibility(View.VISIBLE);
                            currentBalanceMenu.setText("$" + SkyGoDialer.userBalance);
                            menu_name.setText(userDetails.getVoipUserName());
                        }, 100);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            } else {
                menu_linearLayoutMain.setVisibility(View.VISIBLE);
                currentBalanceMenu.setText("$" + SkyGoDialer.userBalance);
                menu_name.setText(userDetails.getVoipUserName());
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "fragment_menu_userbalanced_error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            if (VoipPlan == null) {
                new Thread(() -> {
                    while (VoipPlan == null) {
                        try {
                            VoipPlan = SkyGoDialer.VoipPlan;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.postDelayed(() -> {
                        if (VoipPlan.size() == 0) {
                            listViewVoipPlan.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            VoipPlanArrayAdapter adapter = new VoipPlanArrayAdapter(getApplicationContext(), R.layout.menu_voip_plans, VoipPlan);
                            listViewVoipPlan.setAdapter(adapter);
                            listViewVoipPlan.setVisibility(View.VISIBLE);
                        }
                    }, 100);

                }).start();


            } else {
                if (VoipPlan.size() == 0) {
                    listViewVoipPlan.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    VoipPlanArrayAdapter adapter = new VoipPlanArrayAdapter(getApplicationContext(), R.layout.menu_voip_plans, VoipPlan);
                    listViewVoipPlan.setAdapter(adapter);
                    listViewVoipPlan.setBackgroundColor(getResources().getColor(R.color.white));
                    listViewVoipPlan.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Voip_Select_Plan_Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        try {
            if (mCountry_wise_rateList == null) {
                new Thread(() -> {
                    while (mCountry_wise_rateList == null) {
                        try {
                            Thread.sleep(200);
                            VoipPlan = SkyGoDialer.VoipPlan;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.postDelayed(() -> {
                        if (mCountry_wise_rateList.size() == 0) {
                            checkCountryWisePrice.setVisibility(View.GONE);
                        } else {
                            Country_wise_price_adapter mCountry_wise_price_adapter = new Country_wise_price_adapter(getContext(), mCountry_wise_rateList);
                            snipper_country.setAdapter(mCountry_wise_price_adapter);
                            snipper_country.setVisibility(View.VISIBLE);
                            checkCountryWisePrice.setVisibility(View.VISIBLE);
                        }

                    }, 500);
                }).start();
            } else {
                if (mCountry_wise_rateList.size() == 0) {
                    checkCountryWisePrice.setVisibility(View.GONE);
                } else {
                    Country_wise_price_adapter mCountry_wise_price_adapter = new Country_wise_price_adapter(getContext(), mCountry_wise_rateList);
                    snipper_country.setAdapter(mCountry_wise_price_adapter);
                    snipper_country.setVisibility(View.VISIBLE);
                    checkCountryWisePrice.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listViewVoipPlan.setOnItemClickListener((parent, view1, position, id) -> {
            try {
                @SuppressLint("InflateParams") View contactCarePopUp = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_popup, null);
                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getContext())).setView(contactCarePopUp);
                progressDialog = mBuilder.create();
                Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                progressDialog.show();

                //ID
                TextView cancle = contactCarePopUp.findViewById(R.id.menu_Cancle);
                TextView OK = contactCarePopUp.findViewById(R.id.menu_OK);
                datePicker = contactCarePopUp.findViewById(R.id.simpleDatePicker);
                Date date = new Date();
                datePicker.setMinDate(date.getTime());
                OK.setOnClickListener(v -> {
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth() + 1;
                    int year = datePicker.getYear();

                    GetVoipPlanModel arrayList = (GetVoipPlanModel) listViewVoipPlan.getItemAtPosition(position);
                    String getPlanName = arrayList.getPlanName();
                    String getMonikerValue = arrayList.getMonikerValue();
                    String getValidity = arrayList.getValidity();
                    String getPlanMin = arrayList.getPlanMin();
//                    String getPlanDetails = arrayList.getPlanDetails();
                    String getAmountCharge = arrayList.getAmountCharge();

                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    String amount2 = formatter.format(Double.parseDouble(getAmountCharge));

                    Intent PaymentSummary = new Intent(getContext(), mPayment.class);
                    PaymentSummary.putExtra("Number", getPlanName);
                    PaymentSummary.putExtra("NumberOfDays", getValidity);
                    PaymentSummary.putExtra("AmountCharged", amount2);
                    PaymentSummary.putExtra("RequestedForDtTm", year + "-" + month + "-" + day + " 00:00:00Z");
                    PaymentSummary.putExtra("AppPaymentType", "3");
                    PaymentSummary.putExtra("MonikerValue", getMonikerValue);
                    PaymentSummary.putExtra("PlanMin", getPlanMin);
                    //compare bal and plan
                    PaymentSummary.putExtra("type", "AddPlan");
                    startActivity(PaymentSummary);
                });
                cancle.setOnClickListener(v -> progressDialog.dismiss());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        snipper_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetVoipRateModel co = (GetVoipRateModel) parent.getItemAtPosition(position);
                countryWiseRateDiscription.setText(co.getmRetails() + getResources().getString(R.string.textPrice) + ": " + co.getmPrice());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addBalancedFunction.setOnClickListener(v -> {
            @SuppressLint("InflateParams") View x = LayoutInflater.from(getContext()).inflate(R.layout.dialog_amount_charge_popup, null);
            androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getContext())).setView(x);
            progressDialog = mBuilder.create();
            Objects.requireNonNull(progressDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            progressDialog.show();

            //ID
            TextView cancle = x.findViewById(R.id.balance_Cancle);
            TextView OK = x.findViewById(R.id.balance_OK);
            RadioButton radioButton5 = x.findViewById(R.id.radioButton5Dollar);
            RadioButton radioButton10 = x.findViewById(R.id.radioButton10Dollar);

            radioButton5.setChecked(true);
            amount = "5";

            radioButton5.setOnClickListener(v13 -> {
                radioButton5.setChecked(true);
                radioButton10.setChecked(false);
                amount = "5";
            });
            radioButton10.setOnClickListener(v13 -> {
                radioButton5.setChecked(false);
                radioButton10.setChecked(true);
                amount = "10";
            });

            Date date = new Date();

            OK.setOnClickListener(v1 -> {


                if (amount.equals("") || amount.equals("0") || amount.equals("00") || amount.equals("000")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.textPleaseEnterValidAmount), Toast.LENGTH_SHORT).show();
                    return;
                }
                NumberFormat formatter = NumberFormat.getNumberInstance();
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
                String amount2 = formatter.format(Integer.parseInt(amount));
                Intent PaymentSummary = new Intent(getContext(), mPayment.class);
                PaymentSummary.putExtra("Number", "Add Balance $ " + amount);
                PaymentSummary.putExtra("NumberOfDays", "0");
                PaymentSummary.putExtra("AmountCharged", amount2);
                PaymentSummary.putExtra("RequestedForDtTm", date.getTime());
                PaymentSummary.putExtra("AppPaymentType", "3");
                PaymentSummary.putExtra("MonikerValue", "0");
                PaymentSummary.putExtra("PlanMin", "0");

                //compare bal and plan
                PaymentSummary.putExtra("type", "AddBalance");

                startActivity(PaymentSummary);
            });
            cancle.setOnClickListener(v12 -> progressDialog.dismiss());

        });


        return view;
    }
}
