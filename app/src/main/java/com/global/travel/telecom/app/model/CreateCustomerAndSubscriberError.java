
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CreateCustomerAndSubscriberError {

    @SerializedName("content")
    private String mContent;
    @SerializedName("trid")
    private String mTrid;

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
