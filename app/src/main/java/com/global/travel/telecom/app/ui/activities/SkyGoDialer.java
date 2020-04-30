package com.global.travel.telecom.app.ui.activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.model.Call;
import com.global.travel.telecom.app.model.ContactsModel;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.model.GetActivePromotions;
import com.global.travel.telecom.app.model.GetRateForCountryWise;
import com.global.travel.telecom.app.model.GetVoipPlanModel;
import com.global.travel.telecom.app.model.GetVoipPlans;
import com.global.travel.telecom.app.model.GetVoipRateModel;
import com.global.travel.telecom.app.model.RecentCallHistoryModel;
import com.global.travel.telecom.app.model.RecentSetDataModel;
import com.global.travel.telecom.app.presenter.implementation.AuthenticationPresenter;
import com.global.travel.telecom.app.service.UserDetails;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.global.travel.telecom.app.ui.activities.Fragment_menu.currentBalanceMenu;
import static com.global.travel.telecom.app.ui.activities.Fragment_menu.mProgress;
import static com.global.travel.telecom.app.ui.activities.Fragment_menu.menu_ValiditydaysLeft;
import static com.global.travel.telecom.app.ui.activities.Fragment_menu.menu_ValiditydaysLeftProgressBar;
import static com.global.travel.telecom.app.ui.activities.Fragment_menu.percentageMin;
import static com.global.travel.telecom.app.ui.activities.Fragment_menu.planDeatailsLeft;
import static com.global.travel.telecom.app.ui.activities.Fragment_recent.ListViewRecentCallHistory;

public class SkyGoDialer extends BaseActivity implements Serializable {
    ImageView voip_menu, voip_phone, voip_recent, voip_contacts;
    ViewPager viewPager;
    PagerViewAdepter pagerViewAdepter;
    AuthenticationPresenter authenticationPresenter;
    UserDetails userDetails;
    Context cotxt = this;
    public static String userBalance = "";
    public static ArrayList<ContactsModel> mobileArray = null;
    public static ArrayList<GetVoipPlanModel> VoipPlan = null;
    public static ArrayList<RecentSetDataModel> recentCallHistoryModels = null;
    public static ArrayList<GetVoipRateModel> mCountry_wise_rateList = null;
    public static String InitialQuantity = "0";
    public static String RemainingQuantity = "0";
    public static String totalValidityday = "0";
    public static String leftValidityday = "0";
    List<GetVoipPlanModel> list = null;
    List<RecentSetDataModel> recentList = null;
    List<GetVoipPlans> result = null;
    List<Call> CallHistoryData = null;
    List<GetRateForCountryWise> result1 = null;
    List<GetVoipRateModel> voipRate = null;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Handler handler1 = new Handler(Looper.getMainLooper());


    @Override
    protected int getLayout() {
        return R.layout.activity_sky_go_dialer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sky_go_dialer);
        userDetails = new UserDetails(this);
        voip_menu = findViewById(R.id.voip_menu);
        voip_phone = findViewById(R.id.voip_phone);
        voip_recent = findViewById(R.id.voip_recent);
        voip_contacts = findViewById(R.id.voip_contacts);
        viewPager = findViewById(R.id.fregment_container);
        voip_menu.setBackgroundResource(R.color.white);
        pagerViewAdepter = new PagerViewAdepter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdepter);
        authenticationPresenter = new AuthenticationPresenter(this);


        String getCurrentBalance = "<get-customer-balance version=\"1\">\n" +
                "<authentication>\n" +
                "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                "</authentication>\n" +
                "<subscriberid>" + userDetails.getVoipSubcriberID().trim() + "</subscriberid>\n" +
                "</get-customer-balance>";
        try {
            authenticationPresenter.VoIPAPICall(getCurrentBalance, "getCurrentBalance");
            authenticationPresenter.GetVoipPlan();
            authenticationPresenter.GetVoIPRate();

        } catch (Exception e) {
            Toast.makeText(this, "onCreate authenticationPresenter error" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        voip_menu.setOnClickListener(v -> viewPager.setCurrentItem(0));
        voip_phone.setOnClickListener(v -> viewPager.setCurrentItem(1));
        voip_recent.setOnClickListener(v -> viewPager.setCurrentItem(2));
        voip_contacts.setOnClickListener(v -> viewPager.setCurrentItem(3));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        AsyscGetContact asyscGetContact = new AsyscGetContact();
        asyscGetContact.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {

        if (position == 0) {
            voip_menu.setBackgroundResource(R.color.white);
            voip_phone.setBackgroundResource(R.color.gray);
            voip_recent.setBackgroundResource(R.color.gray);
            voip_contacts.setBackgroundResource(R.color.gray);
        }
        if (position == 1) {
            voip_menu.setBackgroundResource(R.color.gray);
            voip_phone.setBackgroundResource(R.color.white);
            voip_recent.setBackgroundResource(R.color.gray);
            voip_contacts.setBackgroundResource(R.color.gray);
        }
        if (position == 2) {
            voip_menu.setBackgroundResource(R.color.gray);
            voip_phone.setBackgroundResource(R.color.gray);
            voip_recent.setBackgroundResource(R.color.white);
            voip_contacts.setBackgroundResource(R.color.gray);
        }
        if (position == 3) {
            voip_menu.setBackgroundResource(R.color.gray);
            voip_phone.setBackgroundResource(R.color.gray);
            voip_recent.setBackgroundResource(R.color.gray);
            voip_contacts.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDetails = new UserDetails(this);
        String getCurrentBalance = "<get-customer-balance version=\"1\">\n" +
                "<authentication>\n" +
                "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                "</authentication>\n" +
                "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                "</get-customer-balance>";
//        authenticationPresenter.VoIPAPICall(getCurrentBalance, "getCurrentBalance");
//        authenticationPresenter.GetVoipPlan();
//        authenticationPresenter.GetVoIPRate();

        String getActivePromotion = "<get-active-promotions version=\"1\">\n" +
                "<authentication>\n" +
                "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                "</authentication>\n" +
                "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                "</get-active-promotions>";
//        authenticationPresenter.VoIPAPICall(getActivePromotion, "getActivePromotion");
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess(String method, Object response) {
        switch (method) {
            case "CurrentBalance": {
                try {
                    CurrentBalance currentBalance = (CurrentBalance) response;
                    String bal = currentBalance.getGetCustomerBalanceResponse().getClearedBalance().getContent();
                    userBalance = bal.substring(0, bal.length() - 2);
                    currentBalanceMenu.setText("$" + userBalance);
                } catch (Exception e) {
                    showToast("Current Balanced Not show :" + e);
                }
                String getActivePromotion = "<get-active-promotions version=\"1\">\n" +
                        "<authentication>\n" +
                        "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                        "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                        "</authentication>\n" +
                        "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                        "</get-active-promotions>";
                authenticationPresenter.VoIPAPICall(getActivePromotion, "getActivePromotion");
                break;
            }
            case "GetVoipPlanList": {
                result = (List<GetVoipPlans>) response;
                break;
            }
            case "GetVoipRateList": {
                result1 = (List<GetRateForCountryWise>) response;
                break;
            }
            case "getRecentCallHistory": {
                RecentCallHistoryModel re = (RecentCallHistoryModel) response;
                CallHistoryData = re.getGetSubscriberCallHistoryResponse().getCallHistory().getCall();
                Collections.reverse(CallHistoryData);
                recentCallHistoryModels = (ArrayList<RecentSetDataModel>) getRecentCallHistory();
                RecentCallHistoryArrayAdapter adapter = new RecentCallHistoryArrayAdapter(getApplicationContext(), R.layout.recent_call_history_listview, SkyGoDialer.recentCallHistoryModels);
                runOnUiThread(() -> ListViewRecentCallHistory.setAdapter(adapter));
                break;
            }
            case "getActivePromotion": {
                GetActivePromotions getActivePromotion = (GetActivePromotions) response;
//                String i = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().get(0).getInitialQuantity();
//                String r = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().get(0).getRemainingQuantity();
                String i = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().getInitialQuantity();
                String r = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().getRemainingQuantity();
                InitialQuantity = i.substring(0, i.length() - 3);
                RemainingQuantity = r.substring(0, r.length() - 3);
                Date d = new Date();
                String planEndDate = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().getEndTime();
                String planStartDate = getActivePromotion.getGetActivePromotionsResponse().getPromotions().getPromotion().getActiveOffers().getOffer().getStartTime();
                planEndDate = planEndDate.substring(0, planEndDate.length() - 9).trim();
                planStartDate = planStartDate.substring(0, planStartDate.length() - 9).trim();
                totalValidityday = getDiffInTwoDates(planStartDate, planEndDate);
                String currentDate = (String) DateFormat.format("yyyy-MM-dd", d.getTime());
                leftValidityday = getDiffInTwoDates(currentDate, planEndDate);
                planDeatailsLeft.setVisibility(View.VISIBLE);

                if (Integer.parseInt(InitialQuantity) != 0) {
                    planDeatailsLeft.setVisibility(View.VISIBLE);
                    mProgress.setSecondaryProgress(Integer.parseInt(InitialQuantity)); // Secondary Progress
                    mProgress.setMax(Integer.parseInt(InitialQuantity)); // Maximum Progress
                    percentageMin.setText(RemainingQuantity + " min Left");

                    menu_ValiditydaysLeft.setText(leftValidityday + " Day Left");
                    menu_ValiditydaysLeftProgressBar.setMax(Integer.parseInt(totalValidityday));// Maximum Progress
                    menu_ValiditydaysLeftProgressBar.setSecondaryProgress(Integer.parseInt(totalValidityday)); // Secondary Progress
                    final int[] y = {1};
                    final int[] x = {1};

                    new Thread(() -> {
                        while (x[0] <= Integer.parseInt(RemainingQuantity)) {
                            handler.post(() -> {
                                mProgress.setProgress(x[0]);
                                x[0]++;
                            });
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    new Thread(() -> {
                        while (y[0] <= Integer.parseInt(leftValidityday)) {
                            handler1.post(() -> {
                                menu_ValiditydaysLeftProgressBar.setProgress(y[0]);
                                y[0]++;
                            });
                            try {
                                Thread.sleep(60);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                getRecentCallHistoryFunction();
                break;
            }

            case "PromotionNotApplied": {
                planDeatailsLeft.setVisibility(View.GONE);
                getRecentCallHistoryFunction();
                break;
            }
        }
    }

    @Override
    public void onServerError(String method, String errorMessage) {
        switch (method) {
            case "GetVoipPlanList":
            case "GetVoipRateList": {
                showToast(errorMessage);
                break;
            }
        }

    }

    public void getRecentCallHistoryFunction() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        String endDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -30);
        String startDate = dateFormat.format(calendar.getTime());
        String getRecentCallHistory = "<get-subscriber-call-history version=\"1\">\n" +
                "<authentication>\n" +
                "<username>" + userDetails.getVoipCredentailuserName().trim() + "</username>\n" +
                "<password>" + userDetails.getVoipCredentailPassword().trim() + "</password>\n" +
                "</authentication>\n" +
                "<subscriberid>" + userDetails.getVoipSubcriberID() + "</subscriberid>\n" +
                "<start>" + startDate + "</start>\n" +
                "<end>" + endDate + "</end>\n" +
                "</get-subscriber-call-history>";
        authenticationPresenter.VoIPAPICall(getRecentCallHistory, "getRecentCallHistory");
    }

    public String getDiffInTwoDates(String startDate, String endDate) {
        Date date1 = null;
        Date date2 = null;
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = dates.parse(startDate);
            date2 = dates.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Comparing dates
        assert date1 != null;
        long difference = Math.abs(date1.getTime() - date2.getTime());
        long differenceDates = difference / (24 * 60 * 60 * 1000);

        //Convert long to String
        return Long.toString(differenceDates);
    }

    private List<ContactsModel> getAllContacts(Context ctx) {

        List<ContactsModel> list = new ArrayList<>();
        try {
            ContentResolver contentResolver = ctx.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            assert cursor != null;
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        assert cursorInfo != null;
                        if (cursorInfo != null) {
                            while (cursorInfo.moveToNext()) {
                                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                String mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                ContactsModel info = new ContactsModel(name, mobileNumber);
                                list.add(info);
                            }
                        }

                        cursorInfo.close();
                    }
                }
                cursor.close();
            }
            return list;
        } catch (Exception e) {
            showToast("get contect list error:" + e.getMessage());
            e.printStackTrace();
            return list;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyscGetContact extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            VoipPlan = (ArrayList<GetVoipPlanModel>) getAllPlans();
            mCountry_wise_rateList = (ArrayList<GetVoipRateModel>) getAllRatePriceCountryWise();
            mobileArray = (ArrayList<ContactsModel>) getAllContacts(cotxt);
            recentCallHistoryModels = (ArrayList<RecentSetDataModel>) getRecentCallHistory();
            return 0;
        }
    }

    private List<GetVoipPlanModel> getAllPlans() {

        list = new ArrayList<>();
        try {
            while (result == null) {
                Thread.sleep(200);
            }
            for (int i = 0; i < result.size(); i++) {
                GetVoipPlanModel info = new GetVoipPlanModel(result.get(i).getVoipPlan(), result.get(i).getDescription(), String.valueOf(result.get(i).getChargedAmount()), String.valueOf(result.get(i).getMinutes()), String.valueOf(result.get(i).getValidity()), result.get(i).getMoniker(), result.get(i).getVoipId());
                list.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<RecentSetDataModel> getRecentCallHistory() {

        recentList = new ArrayList<>();
        try {
            while (CallHistoryData == null) {
                Thread.sleep(200);
            }
            for (int i = 0; i < CallHistoryData.size(); i++) {
                RecentSetDataModel info = new RecentSetDataModel(CallHistoryData.get(i).getCreateTime(), CallHistoryData.get(i).getDuration(), CallHistoryData.get(i).getLeg2(), CallHistoryData.get(i).getOutcome(), CallHistoryData.get(i).getRetailCharge().getContent());
                recentList.add(info);

            }


        } catch (Exception e) {
            Toast.makeText(this, "getRecentCallHistory list error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return recentList;
    }

    private List<GetVoipRateModel> getAllRatePriceCountryWise() {

        voipRate = new ArrayList<>();
        try {
            while (result1 == null) {
                Thread.sleep(500);
            }
            for (int i = 0; i < result1.size(); i++) {
                GetVoipRateModel info = new GetVoipRateModel(result1.get(i).getCountryCode(), result1.get(i).getCountry(), result1.get(i).getCost(), result1.get(i).getRetail());
                voipRate.add(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return voipRate;
    }

}
