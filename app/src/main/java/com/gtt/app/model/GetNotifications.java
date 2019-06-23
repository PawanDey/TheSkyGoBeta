
package com.gtt.app.model;

import com.google.gson.annotations.SerializedName;

public class GetNotifications {

    @SerializedName("AlertTime")
    public String mAlertTime;
    @SerializedName("CreatedDtTm")
    private String mCreatedDtTm;
    @SerializedName("DealerName")
    public String mDealerName;
    @SerializedName("ID")
    private Long mID;
    @SerializedName("Message")
    public String mMessage;

    public String getAlertTime() {
        return mAlertTime;
    }

    public void setAlertTime(String alertTime) {
        mAlertTime = alertTime;
    }

    public String getCreatedDtTm() {
        return mCreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        mCreatedDtTm = createdDtTm;
    }

    public String getDealerName() {
        return mDealerName;
    }

    public void setDealerName(String dealerName) {
        mDealerName = dealerName;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long iD) {
        mID = iD;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
