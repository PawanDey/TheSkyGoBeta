
package com.gtt.app.model;


import com.google.gson.annotations.SerializedName;

public class Table {

    @SerializedName("AccountBalance")
    private Double mAccountBalance;
    @SerializedName("ResponseCode")
    private Long mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;

    public Double getAccountBalance() {
        return mAccountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        mAccountBalance = accountBalance;
    }

    public Long getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(Long responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }

}
