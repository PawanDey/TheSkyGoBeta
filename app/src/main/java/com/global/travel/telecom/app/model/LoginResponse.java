
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("DealerName")
    private String mDealerName;
    @SerializedName("EmailID")
    private String mEmailID;
    @SerializedName("PaypalTransactionFee")
    private Double mPaypalTransactionFee;
    @SerializedName("TokenID")
    private String mTokenID;
    @SerializedName("TxnSeriesPrefix")
    private String mTxnSeriesPrefix;
    @SerializedName("UserName")
    private String mUserName;
    @SerializedName("DealerID")
    private String mDealerID;

    public String getDealerName() {
        return mDealerName;
    }

    public void setDealerName(String dealerName) {
        mDealerName = dealerName;
    }

    public String getEmailID() {
        return mEmailID;
    }

    public void setEmailID(String emailID) {
        mEmailID = emailID;
    }

    public Double getPaypalTransactionFee() {
        return mPaypalTransactionFee;
    }

    public void setPaypalTransactionFee(Double paypalTransactionFee) {
        mPaypalTransactionFee = paypalTransactionFee;
    }

    public String getTokenID() {
        return mTokenID;
    }

    public void setTokenID(String tokenID) {
        mTokenID = tokenID;
    }

    public String getTxnSeriesPrefix() {
        return mTxnSeriesPrefix;
    }

    public void setTxnSeriesPrefix(String txnSeriesPrefix) {
        mTxnSeriesPrefix = txnSeriesPrefix;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getDealerID() {
        return mDealerID;
    }

    public void setDealerID(String DealerID) {
        mDealerID = DealerID;
    }
}
