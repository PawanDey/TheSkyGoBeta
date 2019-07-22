
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class GetSubscriberResponse {


    @SerializedName("ActDate")
    private String mActDate;
    @SerializedName("DealerName")
    private String mDealerName;
    @SerializedName("GoodUntil")
    private String mGoodUntil;
    @SerializedName("MSISDN")
    private String mMSISDN;
    @SerializedName("ParentDealerName")
    private String mParentDealerName;
    @SerializedName("SIM")
    private String mSIM;
    @SerializedName("Status")
    private String mStatus;




    public String getActDate() {
        return mActDate;
    }

    public void setActDate(String actDate) {
        mActDate = actDate;
    }

    public String getDealerName() {
        return mDealerName;
    }

    public void setDealerName(String dealerName) {
        mDealerName = dealerName;
    }

    public String getGoodUntil() {
        return mGoodUntil;
    }

    public void setGoodUntil(String goodUntil) {
        mGoodUntil = goodUntil;
    }

    public String getMSISDN() {
        return mMSISDN;
    }

    public void setMSISDN(String mSISDN) {
        mMSISDN = mSISDN;
    }

    public String getParentDealerName() {
        return mParentDealerName;
    }

    public void setParentDealerName(String parentDealerName) {
        mParentDealerName = parentDealerName;
    }

    public String getSIM() {
        return mSIM;
    }

    public void setSIM(String sIM) {
        mSIM = sIM;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }



}
