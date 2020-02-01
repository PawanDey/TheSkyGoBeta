
package com.global.travel.telecom.app.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CallHistory {

    @SerializedName("call")
    private List<Call> mCall;

    public List<Call> getCall() {
        return mCall;
    }

    public void setCall(List<Call> call) {
        mCall = call;
    }

}
