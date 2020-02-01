
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("TokenID")
    @Expose
    private String tokenID;
    @SerializedName("DealerName")
    @Expose
    private String dealerName;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("TxnSeriesPrefix")
    @Expose
    private String txnSeriesPrefix;
    @SerializedName("PaypalTransactionFee")
    @Expose
    private String paypalTransactionFee;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("VoIPCredentialID")
    @Expose
    private String voIPCredentialID;
    @SerializedName("VoIPUserName")
    @Expose
    private String voIPUserName;
    @SerializedName("VoIPPassword")
    @Expose
    private String voIPPassword;
    @SerializedName("VoIPDistributorId")
    @Expose
    private String voIPDistributorId;
    @SerializedName("VoIPCreditBasis")
    @Expose
    private String voIPCreditBasis;
    @SerializedName("VoIPCreditLimit")
    @Expose
    private String voIPCreditLimit;
    @SerializedName("VoIPWarningTrigger")
    @Expose
    private String voIPWarningTrigger;
    @SerializedName("VoIPCustomerGroup")
    @Expose
    private String voIPCustomerGroup;
    @SerializedName("VoIPEnableSipRegistration")
    @Expose
    private String voIPEnableSipRegistration;
    @SerializedName("VoIPPreferSip")
    @Expose
    private String voIPPreferSip;
    @SerializedName("VoIPVoicemailEnable")
    @Expose
    private String voIPVoicemailEnable;
    @SerializedName("VoIPVoicemailTimeout")
    @Expose
    private String voIPVoicemailTimeout;
    @SerializedName("VoIPNotifyMissedCall")
    @Expose
    private String voIPNotifyMissedCall;
    @SerializedName("VoIPSendChargeNotifications")
    @Expose
    private String voIPSendChargeNotifications;
    @SerializedName("VoIPSendCreditNotifications")
    @Expose
    private String voIPSendCreditNotifications;
    @SerializedName("VoIPForwardTo")
    @Expose
    private String voIPForwardTo;
    @SerializedName("VoIPWithholdCLI")
    @Expose
    private String voIPWithholdCLI;
    @SerializedName("VoIPForwardCallback")
    @Expose
    private String voIPForwardCallback;
    @SerializedName("VoIPAutoCLI")
    @Expose
    private String voIPAutoCLI;
    @SerializedName("VoIPBlockGPRS")
    @Expose
    private String voIPBlockGPRS;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getTxnSeriesPrefix() {
        return txnSeriesPrefix;
    }

    public void setTxnSeriesPrefix(String txnSeriesPrefix) {
        this.txnSeriesPrefix = txnSeriesPrefix;
    }

    public String getPaypalTransactionFee() {
        return paypalTransactionFee;
    }

    public void setPaypalTransactionFee(String paypalTransactionFee) {
        this.paypalTransactionFee = paypalTransactionFee;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getVoIPCredentialID() {
        return voIPCredentialID;
    }

    public void setVoIPCredentialID(String voIPCredentialID) {
        this.voIPCredentialID = voIPCredentialID;
    }

    public String getVoIPUserName() {
        return voIPUserName;
    }

    public void setVoIPUserName(String voIPUserName) {
        this.voIPUserName = voIPUserName;
    }

    public String getVoIPPassword() {
        return voIPPassword;
    }

    public void setVoIPPassword(String voIPPassword) {
        this.voIPPassword = voIPPassword;
    }

    public String getVoIPDistributorId() {
        return voIPDistributorId;
    }

    public void setVoIPDistributorId(String voIPDistributorId) {
        this.voIPDistributorId = voIPDistributorId;
    }

    public String getVoIPCreditBasis() {
        return voIPCreditBasis;
    }

    public void setVoIPCreditBasis(String voIPCreditBasis) {
        this.voIPCreditBasis = voIPCreditBasis;
    }

    public String getVoIPCreditLimit() {
        return voIPCreditLimit;
    }

    public void setVoIPCreditLimit(String voIPCreditLimit) {
        this.voIPCreditLimit = voIPCreditLimit;
    }

    public String getVoIPWarningTrigger() {
        return voIPWarningTrigger;
    }

    public void setVoIPWarningTrigger(String voIPWarningTrigger) {
        this.voIPWarningTrigger = voIPWarningTrigger;
    }

    public String getVoIPCustomerGroup() {
        return voIPCustomerGroup;
    }

    public void setVoIPCustomerGroup(String voIPCustomerGroup) {
        this.voIPCustomerGroup = voIPCustomerGroup;
    }

    public String getVoIPEnableSipRegistration() {
        return voIPEnableSipRegistration;
    }

    public void setVoIPEnableSipRegistration(String voIPEnableSipRegistration) {
        this.voIPEnableSipRegistration = voIPEnableSipRegistration;
    }

    public String getVoIPPreferSip() {
        return voIPPreferSip;
    }

    public void setVoIPPreferSip(String voIPPreferSip) {
        this.voIPPreferSip = voIPPreferSip;
    }

    public String getVoIPVoicemailEnable() {
        return voIPVoicemailEnable;
    }

    public void setVoIPVoicemailEnable(String voIPVoicemailEnable) {
        this.voIPVoicemailEnable = voIPVoicemailEnable;
    }

    public String getVoIPVoicemailTimeout() {
        return voIPVoicemailTimeout;
    }

    public void setVoIPVoicemailTimeout(String voIPVoicemailTimeout) {
        this.voIPVoicemailTimeout = voIPVoicemailTimeout;
    }

    public String getVoIPNotifyMissedCall() {
        return voIPNotifyMissedCall;
    }

    public void setVoIPNotifyMissedCall(String voIPNotifyMissedCall) {
        this.voIPNotifyMissedCall = voIPNotifyMissedCall;
    }

    public String getVoIPSendChargeNotifications() {
        return voIPSendChargeNotifications;
    }

    public void setVoIPSendChargeNotifications(String voIPSendChargeNotifications) {
        this.voIPSendChargeNotifications = voIPSendChargeNotifications;
    }

    public String getVoIPSendCreditNotifications() {
        return voIPSendCreditNotifications;
    }

    public void setVoIPSendCreditNotifications(String voIPSendCreditNotifications) {
        this.voIPSendCreditNotifications = voIPSendCreditNotifications;
    }

    public String getVoIPForwardTo() {
        return voIPForwardTo;
    }

    public void setVoIPForwardTo(String voIPForwardTo) {
        this.voIPForwardTo = voIPForwardTo;
    }

    public String getVoIPWithholdCLI() {
        return voIPWithholdCLI;
    }

    public void setVoIPWithholdCLI(String voIPWithholdCLI) {
        this.voIPWithholdCLI = voIPWithholdCLI;
    }

    public String getVoIPForwardCallback() {
        return voIPForwardCallback;
    }

    public void setVoIPForwardCallback(String voIPForwardCallback) {
        this.voIPForwardCallback = voIPForwardCallback;
    }

    public String getVoIPAutoCLI() {
        return voIPAutoCLI;
    }

    public void setVoIPAutoCLI(String voIPAutoCLI) {
        this.voIPAutoCLI = voIPAutoCLI;
    }

    public String getVoIPBlockGPRS() {
        return voIPBlockGPRS;
    }

    public void setVoIPBlockGPRS(String voIPBlockGPRS) {
        this.voIPBlockGPRS = voIPBlockGPRS;
    }

}