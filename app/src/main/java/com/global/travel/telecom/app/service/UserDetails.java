package com.global.travel.telecom.app.service;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetails {

    Context context;
    SharedPreferences sharedPreferences;
    private int RechargeStatus;
    private int RechargeStatusDataServer;
    private String ActivationDate;
    private String ActivationDateDataServer;
    private String LanguageSelect;
    private String MacAddress;
    private String MSISDN;
    private String MSISDNDataServer;
    private String PaypalTransactionFee;
    private String TxnSeriesPrefix;
    private String UserName;
    private String TokenID;
    private String TokenIDDataServer;
    private String VoipSubcriberID;
    private String UserId;
    private String UserIdDataServer;
    private String VoipCredentailuserName;
    private String VoipCredentailPassword;
    private String VoipUserName;

    public int getRechargeStatusDataServer() {
        RechargeStatusDataServer = sharedPreferences.getInt("RechargeStatusDataServer", 1);
        return RechargeStatusDataServer;
    }

    public void setRechargeStatusDataServer(int RechargeStatusDataServer) {
        sharedPreferences.edit().putInt("RechargeStatusDataServer", RechargeStatusDataServer).commit();
    }

    public String getActivationDateDataServer() {
        ActivationDateDataServer = sharedPreferences.getString("SIMActivationDateDataServer", "");
        return ActivationDateDataServer;
    }

    public void setActivationDateDataServer(String ActivationDateDataServer) {
        sharedPreferences.edit().putString("SIMActivationDateDataServer", ActivationDateDataServer).commit();
    }

    public String getMSISDNDataServer() {
        MSISDNDataServer = sharedPreferences.getString("MSISDNDataServer", "");
        return MSISDNDataServer;
    }

    public void setMSISDNDataServer(String MSISDNDataServer) {
        sharedPreferences.edit().putString("MSISDNDataServer", MSISDNDataServer).commit();
    }

    public String getTokenIDDataServer() {
        TokenIDDataServer = sharedPreferences.getString("TokenIDDataServer", "");
        return TokenIDDataServer;
    }

    public void setTokenIDDataServer(String TokenIDDataServer) {
        sharedPreferences.edit().putString("TokenIDDataServer", TokenIDDataServer).commit();
    }

    public String getUserIdDataServer() {
        UserIdDataServer = sharedPreferences.getString("UserIdDataServer", "");
        return UserIdDataServer;
    }

    public void setUserIdDataServer(String userIdDataServer) {
        sharedPreferences.edit().putString("UserIdDataServer", UserIdDataServer).commit();
    }

    public String getVoipUserName() {
        VoipUserName = sharedPreferences.getString("VoipUserName", "");
        return VoipUserName;
    }

    public void setVoipUserName(String VoipUserName) {
        sharedPreferences.edit().putString("VoipUserName", VoipUserName).commit();
    }

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

    public String getVoipCredentailuserName() {
        VoipCredentailuserName = sharedPreferences.getString("VoipCredentailuserName", "");
        return VoipCredentailuserName;
    }

    public void setVoipCredentailuserName(String VoipCredentailuserName) {
        sharedPreferences.edit().putString("VoipCredentailuserName", VoipCredentailuserName).commit();
    }

    public String getVoipCredentailPassword() {
        VoipCredentailPassword = sharedPreferences.getString("VoipCredentailPassword", "");
        return VoipCredentailPassword;
    }

    public void setVoipCredentailPassword(String VoipCredentailPassword) {
        sharedPreferences.edit().putString("VoipCredentailPassword", VoipCredentailPassword).commit();
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


    public String getLanguageSelect() {
        LanguageSelect = sharedPreferences.getString("LanguageSelect", "");
        return LanguageSelect;
    }

    public void setLanguageSelect(String LanguageSelect) {
        sharedPreferences.edit().putString("LanguageSelect", LanguageSelect).commit();
    }


    public int getRechargeStatus() {
        RechargeStatus = sharedPreferences.getInt("RechargeStatus", 1);
        return RechargeStatus;
    }

    public void setRechargeStatus(int rechargeStatus) {
        sharedPreferences.edit().putInt("RechargeStatus", rechargeStatus).commit();
    }


    public String getMSISDN() {
        MSISDN = sharedPreferences.getString("MSISDN", "");
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        sharedPreferences.edit().putString("MSISDN", MSISDN).commit();
    }


    public String getActivationDate() {
        ActivationDate = sharedPreferences.getString("SIMActivationDate", "");
        return ActivationDate;
    }

    public void setActivationDate(String ActivationDate) {
        sharedPreferences.edit().putString("SIMActivationDate", ActivationDate).commit();

    }


    public String getMacAddress() {
        MacAddress = sharedPreferences.getString("MacAddress", "");
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        sharedPreferences.edit().putString("MacAddress", MacAddress).commit();

    }


    public String getVoipCustomerID() {
        String voipCustomerID = sharedPreferences.getString("VoipCustomerID", "");
        return voipCustomerID;
    }

    public void setVoipCustomerID(String VoipCustomerID) {
        sharedPreferences.edit().putString("VoipCustomerID", VoipCustomerID).commit();

    }

    public String getVoipSubcriberID() {
        VoipSubcriberID = sharedPreferences.getString("VoipSubscriberID", "");
        return VoipSubcriberID;
    }

    public void setVoipSubcriberID(String VoipSubcriberID) {
        sharedPreferences.edit().putString("VoipSubscriberID", VoipSubcriberID).commit();

    }

    public String getUserId() {
        UserId = sharedPreferences.getString("UserId", "");
        return UserId;
    }

    public void setUserId(String UserId) {
        sharedPreferences.edit().putString("UserId", UserId).commit();
    }

    public UserDetails(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("LanguageSelect", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("SIMActivationDate", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("MacAddress", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("RechargeStatus", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("MSISDN", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("TxnSeriesPrefix", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("PaypalTransactionFee", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("TokenID", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("UserName", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("VoipSubscriberID", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("VoipCustomerID", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("UserId", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("VoipCredentailuserName", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("VoipCredentailPassword", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("VoipUserName", Context.MODE_PRIVATE);

    }

}
