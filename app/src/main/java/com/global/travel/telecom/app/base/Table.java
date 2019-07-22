
package com.global.travel.telecom.app.base;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Table {

    @SerializedName("ResponseCode")
    private Long mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;

    public Long getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(Long responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }

}
