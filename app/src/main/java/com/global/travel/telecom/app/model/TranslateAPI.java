
package com.global.travel.telecom.app.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TranslateAPI {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("Payload")
    private List<String> mPayload;
    @SerializedName("responseCode")
    private String mResponseCode;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<String> getPayload() {
        return mPayload;
    }

    public void setPayload(List<String> payload) {
        mPayload = payload;
    }

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String responseCode) {
        mResponseCode = responseCode;
    }

}
