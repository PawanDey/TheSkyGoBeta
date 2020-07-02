
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Offer {

    @SerializedName("call-type-group")
    private String mCallTypeGroup;
    @SerializedName("elementid")
    private String mElementid;
    @SerializedName("end-time")
    private String mEndTime;
    @SerializedName("initial-quantity")
    private String mInitialQuantity;
    @SerializedName("offerid")
    private String mOfferid;
    @SerializedName("remaining-quantity")
    private String mRemainingQuantity;
    @SerializedName("start-time")
    private String mStartTime;

    public String getCallTypeGroup() {
        return mCallTypeGroup;
    }

    public void setCallTypeGroup(String callTypeGroup) {
        mCallTypeGroup = callTypeGroup;
    }

    public String getElementid() {
        return mElementid;
    }

    public void setElementid(String elementid) {
        mElementid = elementid;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public String getInitialQuantity() {
        return mInitialQuantity;
    }

    public void setInitialQuantity(String initialQuantity) {
        mInitialQuantity = initialQuantity;
    }

    public String getOfferid() {
        return mOfferid;
    }

    public void setOfferid(String offerid) {
        mOfferid = offerid;
    }

    public String getRemainingQuantity() {
        return mRemainingQuantity;
    }

    public void setRemainingQuantity(String remainingQuantity) {
        mRemainingQuantity = remainingQuantity;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

}
