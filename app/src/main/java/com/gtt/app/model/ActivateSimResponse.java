
package com.gtt.app.model;

import com.google.gson.annotations.SerializedName;

public class ActivateSimResponse {

    @SerializedName("ResponseCode")
    private Long mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;
    @SerializedName("RatePerDay")
    private String mRatePerDay;

    @SerializedName("NumberOfDays")
    private String mNumberOfDays;

    @SerializedName("LastValidityDate")
    private String mLastValidityDate;

    public String getmLastValidityDate () {return mLastValidityDate;}

    public void setmLastValidityDate (String LastDate) {mLastValidityDate=LastDate;}


    public String getNumberOfDays () {return  mNumberOfDays;}

    public void setNumberOfDays (String NumberOfDays) {mNumberOfDays=NumberOfDays;}

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

    public  String  getmRatePerDay () {return  mRatePerDay;}

    public void setmRatePerDay (String ratePerDay) {mRatePerDay=ratePerDay;}
}
