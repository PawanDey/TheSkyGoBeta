
package com.global.travel.telecom.app.model;


import com.google.gson.annotations.SerializedName;


public class UpdateFundReq {

    @SerializedName("Id")
    private String mId;
    @SerializedName("PayPalResponse")
    private String mPayPalResponse;
    @SerializedName("PaypalReferenceID")
    private String mPaypalReferenceID;
    @SerializedName("RequestStatusID")
    private String mRequestStatusID;
    @SerializedName("TokenID")
    private String mTokenID;
    @SerializedName("TransactionReferenceID")
    private String mTransactionReferenceID;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPayPalResponse() {
        return mPayPalResponse;
    }

    public void setPayPalResponse(String payPalResponse) {
        mPayPalResponse = payPalResponse;
    }

    public String getPaypalReferenceID() {
        return mPaypalReferenceID;
    }

    public void setPaypalReferenceID(String paypalReferenceID) {
        mPaypalReferenceID = paypalReferenceID;
    }

    public String getRequestStatusID() {
        return mRequestStatusID;
    }

    public void setRequestStatusID(String requestStatusID) {
        mRequestStatusID = requestStatusID;
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

}
