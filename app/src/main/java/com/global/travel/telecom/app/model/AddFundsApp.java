
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class AddFundsApp {

    @SerializedName("AmountCharged")
    private String mAmountCharged;
    @SerializedName("DealerID")
    private String mDealerID;
    @SerializedName("DebitCredit")
    private String mDebitCredit;
    @SerializedName("IMEI")
    private String mIMEI;
    @SerializedName("Latitude")
    private String mLatitude;
    @SerializedName("Longitude")
    private String mLongitude;
    @SerializedName("MacID")
    private String mMacID;
    @SerializedName("PayPalRefNo")
    private String mPayPalRefNo;
    @SerializedName("PaymentMode")
    private String mPaymentMode;
    @SerializedName("Remarks")
    private String mRemarks;
    @SerializedName("RequestedDevice")
    private String mRequestedDevice;
    @SerializedName("RequestedIP")
    private String mRequestedIP;
    @SerializedName("RequestedOS")
    private String mRequestedOS;
    @SerializedName("ServiceCharge")
    private String mServiceCharge;
    @SerializedName("TokenID")
    private String mTokenID;
    @SerializedName("TransactionReferenceID")
    private String mTransactionReferenceID;
    @SerializedName("TransactionType")
    private String mTransactionType;

    public String getAmountCharged() {
        return mAmountCharged;
    }

    public void setAmountCharged(String amountCharged) {
        mAmountCharged = amountCharged;
    }

    public String getDealerID() {
        return mDealerID;
    }

    public void setDealerID(String dealerID) {
        mDealerID = dealerID;
    }

    public String getDebitCredit() {
        return mDebitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        mDebitCredit = debitCredit;
    }

    public String getIMEI() {
        return mIMEI;
    }

    public void setIMEI(String iMEI) {
        mIMEI = iMEI;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getMacID() {
        return mMacID;
    }

    public void setMacID(String macID) {
        mMacID = macID;
    }

    public String getPayPalRefNo() {
        return mPayPalRefNo;
    }

    public void setPayPalRefNo(String payPalRefNo) {
        mPayPalRefNo = payPalRefNo;
    }

    public String getPaymentMode() {
        return mPaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        mPaymentMode = paymentMode;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String remarks) {
        mRemarks = remarks;
    }

    public String getRequestedDevice() {
        return mRequestedDevice;
    }

    public void setRequestedDevice(String requestedDevice) {
        mRequestedDevice = requestedDevice;
    }

    public String getRequestedIP() {
        return mRequestedIP;
    }

    public void setRequestedIP(String requestedIP) {
        mRequestedIP = requestedIP;
    }

    public String getRequestedOS() {
        return mRequestedOS;
    }

    public void setRequestedOS(String requestedOS) {
        mRequestedOS = requestedOS;
    }

    public String getServiceCharge() {
        return mServiceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        mServiceCharge = serviceCharge;
    }

    public String getTokenID() {
        return mTokenID;
    }

    public void setTokenID(String tokenID) {
        mTokenID = tokenID;
    }

    public String getTransactionReferenceID() {
        return mTransactionReferenceID;
    }

    public void setTransactionReferenceID(String transactionReferenceID) {
        mTransactionReferenceID = transactionReferenceID;
    }

    public String getTransactionType() {
        return mTransactionType;
    }

    public void setTransactionType(String transactionType) {
        mTransactionType = transactionType;
    }

}
