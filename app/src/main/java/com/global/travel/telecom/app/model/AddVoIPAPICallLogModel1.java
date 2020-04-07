
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddVoIPAPICallLogModel1 {

    @SerializedName("APIResponse")
    private String APIResponse;
    @SerializedName("APIName")
    private String APIName;
    @SerializedName("Platform")
    private String Platform;
    @SerializedName("PlanID")
    private String PlanID;
    @SerializedName("PlanType")
    private String PlanType;
    @SerializedName("UserID")
    private String UserID;
    @SerializedName("APIRequest")
    private String APIRequest;
    @SerializedName("ParchaseStatus")
    private String ParchaseStatus;
    @SerializedName("TxnRefNo")
    private String TxnRefNo;

    public String getTxnRefNo() {
        return TxnRefNo;
    }

    public void setTxnRefNo(String txnRefNo) {
        TxnRefNo = txnRefNo;
    }

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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAPIRequest() {
        return APIRequest;
    }

    public void setAPIRequest(String APIRequest) {
        this.APIRequest = APIRequest;
    }

    public String getAPIResponse() {
        return APIResponse;
    }

    public void setAPIResponse(String APIResponse) {
        this.APIResponse = APIResponse;
    }

    public String getAPIName() {
        return APIName;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }
}
