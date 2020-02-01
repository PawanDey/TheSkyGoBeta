
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetSubscriberCallHistoryResponse {

    @SerializedName("call-history")
    private CallHistory mCallHistory;
    @SerializedName("trid")
    private String mTrid;

    public CallHistory getCallHistory() {
        return mCallHistory;
    }

    public void setCallHistory(CallHistory callHistory) {
        mCallHistory = callHistory;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
