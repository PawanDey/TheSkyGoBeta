package com.gtt.app.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.gtt.app.ui.activities.Recharge;

public class UserDetails {

    Context context;
    SharedPreferences sharedPreferences;

    public void removeTokenID() {
        sharedPreferences.edit().clear().commit();
    }

    public String getTokenID() {
        TokenID = sharedPreferences.getString("TokenID", "");
        return TokenID;
    }

    public void setTokenID(String TokenID) {
        this.TokenID = TokenID;
        sharedPreferences.edit().putString("TokenID", TokenID).commit();

    }

    public String getUserName() {
        UserName = sharedPreferences.getString("UserName", "");
        return UserName;
    }

    public void setUserName(String UserName) {
        sharedPreferences.edit().putString("UserName", UserName).commit();
    }


    public String getTxnSeriesPrefix() {
        TxnSeriesPrefix = sharedPreferences.getString("TxnSeriesPrefix", "");
        return TxnSeriesPrefix;
    }

    public void setTxnSeriesPrefix(String TxnSeriesPrefix) {
        sharedPreferences.edit().putString("TxnSeriesPrefix", TxnSeriesPrefix).commit();
    }

    public String getPaypalTransactionFee() {
        PaypalTransactionFee = sharedPreferences.getString("PaypalTransactionFee", "");
        return PaypalTransactionFee;
    }

    public void setPaypalTransactionFee(String paypalTransactionFee) {
        sharedPreferences.edit().putString("PaypalTransactionFee", paypalTransactionFee).commit();
    }

    private String TokenID;
    private String UserName;
    private String TxnSeriesPrefix;
    private String PaypalTransactionFee;

    public int getRechargeStatus() {
        RechargeStatus = sharedPreferences.getInt("RechargeStatus", 1);
        return RechargeStatus;
    }

    public void setRechargeStatus(int rechargeStatus) {
        sharedPreferences.edit().putInt("RechargeStatus", RechargeStatus).commit();
    }


    public String getMSISDN() {
        MSISDN = sharedPreferences.getString("MSISDN", "");
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        sharedPreferences.edit().putString("MSISDN", MSISDN).commit();
    }

    private String MSISDN;
    private int RechargeStatus;
    private String MacAddress;

    public String getMacAddress() {
        MacAddress = sharedPreferences.getString("MacAddress", "");
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        sharedPreferences.edit().putString("MacAddress", MacAddress).commit();

    }

    public UserDetails(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("MacAddress", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("RechargeStatus", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("MSISDN", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("TxnSeriesPrefix", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("PaypalTransactionFee", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("TokenID", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("UserName", Context.MODE_PRIVATE);
    }

}
