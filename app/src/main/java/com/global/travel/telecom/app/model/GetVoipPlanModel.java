package com.global.travel.telecom.app.model;


public class GetVoipPlanModel {

    private String planName, planDetails, AmountCharge, Validity, planMin, monikerValue, VoipID;

    public String getVoipID() {
        return VoipID;
    }

    public void setVoipID(String voipID) {
        VoipID = voipID;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(String planDetails) {
        this.planDetails = planDetails;
    }

    public String getAmountCharge() {
        return AmountCharge;
    }

    public void setAmountCharge(String amountCharge) {
        AmountCharge = amountCharge;
    }

    public String getValidity() {
        return Validity;
    }

    public void setValidity(String validity) {
        Validity = validity;
    }

    public String getPlanMin() {
        return planMin;
    }

    public void setPlanMin(String planMin) {
        this.planMin = planMin;
    }

    public String getMonikerValue() {
        return monikerValue;
    }

    public void setMonikerValue(String monikerValue) {
        this.monikerValue = monikerValue;
    }


    public GetVoipPlanModel(String planName, String planDetails, String AmountCharge, String planMin, String validity, String monikerValue, String VoipID) {
        this.planName = planName;
        this.planDetails = planDetails;
        this.AmountCharge = AmountCharge;
        this.planMin = planMin;
        this.Validity = validity;
        this.monikerValue = monikerValue;
        this.VoipID = VoipID;

    }
}
