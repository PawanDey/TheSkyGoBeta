
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class ActivateSimResponse {

    @SerializedName("ResponseCode")
    private Long mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;
    @SerializedName("RatePerDay")
    private double mRatePerDay;
    @SerializedName("NumberOfDays")
    private String mNumberOfDays;
    @SerializedName("LastValidityDate")
    private String mLastValidityDate;
    @SerializedName("PromotionName")
    private String mPromotionName;

    public String getmPromotionName() {
        return mPromotionName;
    }

    public void setmPromotionName(String mPromotionName) {
        this.mPromotionName = mPromotionName;
    }

    public String getmLastValidityDate() {
        return mLastValidityDate;
    }

    public void setmLastValidityDate(String LastDate) {
        mLastValidityDate = LastDate;
    }

    public String getNumberOfDays() {
        return mNumberOfDays;
    }

    public void setNumberOfDays(String NumberOfDays) {
        mNumberOfDays = NumberOfDays;
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

    public double getmRatePerDay() {
        return mRatePerDay;
    }

    public void setmRatePerDay(double ratePerDay) {
        mRatePerDay = ratePerDay;
    }
}
