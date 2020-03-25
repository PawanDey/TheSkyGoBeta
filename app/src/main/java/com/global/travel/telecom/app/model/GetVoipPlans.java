
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetVoipPlans {

    @SerializedName("ChargedAmount")
    private Double mChargedAmount;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("Minutes")
    private Long mMinutes;
    @SerializedName("Moniker")
    private String mMoniker;
    @SerializedName("Validity")
    private Long mValidity;
    @SerializedName("VoipPlan")
    private String mVoipPlan;
    @SerializedName("VoipId")
    private String VoipId;

    public String getVoipId() {
        return VoipId;
    }

    public void setVoipId(String voipId) {
        VoipId = voipId;
    }

    public Double getChargedAmount() {
        return mChargedAmount;
    }

    public void setChargedAmount(Double chargedAmount) {
        mChargedAmount = chargedAmount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Long getMinutes() {
        return mMinutes;
    }

    public void setMinutes(Long minutes) {
        mMinutes = minutes;
    }

    public String getMoniker() {
        return mMoniker;
    }

    public void setMoniker(String moniker) {
        mMoniker = moniker;
    }

    public Long getValidity() {
        return mValidity;
    }

    public void setValidity(Long validity) {
        mValidity = validity;
    }

    public String getVoipPlan() {
        return mVoipPlan;
    }

    public void setVoipPlan(String voipPlan) {
        mVoipPlan = voipPlan;
    }

}
