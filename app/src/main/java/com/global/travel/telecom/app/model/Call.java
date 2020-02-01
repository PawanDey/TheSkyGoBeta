
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Call {

    @SerializedName("create-time")
    private String mCreateTime;
    @SerializedName("duration")
    private String mDuration;
    @SerializedName("id")
    private String mId;
    @SerializedName("internal-outcome")
    private String mInternalOutcome;
    @SerializedName("leg1")
    private String mLeg1;
    @SerializedName("leg1-country")
    private Leg1Country mLeg1Country;
    @SerializedName("leg2")
    private String mLeg2;
    @SerializedName("leg2-country")
    private Leg2Country mLeg2Country;
    @SerializedName("narrative")
    private String mNarrative;
    @SerializedName("outcome")
    private String mOutcome;
    @SerializedName("retail-charge")
    private RetailCharge mRetailCharge;
    @SerializedName("type")
    private String mType;

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInternalOutcome() {
        return mInternalOutcome;
    }

    public void setInternalOutcome(String internalOutcome) {
        mInternalOutcome = internalOutcome;
    }

    public String getLeg1() {
        return mLeg1;
    }

    public void setLeg1(String leg1) {
        mLeg1 = leg1;
    }

    public Leg1Country getLeg1Country() {
        return mLeg1Country;
    }

    public void setLeg1Country(Leg1Country leg1Country) {
        mLeg1Country = leg1Country;
    }

    public String getLeg2() {
        return mLeg2;
    }

    public void setLeg2(String leg2) {
        mLeg2 = leg2;
    }

    public Leg2Country getLeg2Country() {
        return mLeg2Country;
    }

    public void setLeg2Country(Leg2Country leg2Country) {
        mLeg2Country = leg2Country;
    }

    public String getNarrative() {
        return mNarrative;
    }

    public void setNarrative(String narrative) {
        mNarrative = narrative;
    }

    public String getOutcome() {
        return mOutcome;
    }

    public void setOutcome(String outcome) {
        mOutcome = outcome;
    }

    public RetailCharge getRetailCharge() {
        return mRetailCharge;
    }

    public void setRetailCharge(RetailCharge retailCharge) {
        mRetailCharge = retailCharge;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}
