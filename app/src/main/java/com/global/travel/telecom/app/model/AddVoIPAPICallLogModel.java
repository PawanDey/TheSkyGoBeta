
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
