
package com.global.travel.telecom.app.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class AddFundsResponse {

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    @SerializedName("RequestId")
    private  String RequestId;


}
