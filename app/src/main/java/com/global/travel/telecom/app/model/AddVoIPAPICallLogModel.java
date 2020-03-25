
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddVoIPAPICallLogModel {

    @SerializedName("APIrequest")
    private String APIrequest;
    @SerializedName("APIresponse")
    private String APIresponse;
    @SerializedName("APIName")
    private String APIName;
    @SerializedName("PlanID")
    private String PlanID;
    @SerializedName("PlanType")
    private String PlanType;
    @SerializedName("ParchaseStatus")
    private String ParchaseStatus;

    public String getParchaseStatus() {
        return ParchaseStatus;
    }

    public void setParchaseStatus(String parchaseStatus) {
        ParchaseStatus = parchaseStatus;
    }

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }

    public String getPlanType() {
        return PlanType;
    }

    public void setPlanType(String planType) {
        PlanType = planType;
    }


    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }


    public String getAPIrequest() {
        return APIrequest;
    }

    public void setAPIrequest(String APIrequest) {
        this.APIrequest = APIrequest;
    }

    public String getAPIresponse() {
        return APIresponse;
    }

    public void setAPIresponse(String APIresponse) {
        this.APIresponse = APIresponse;
    }
}
