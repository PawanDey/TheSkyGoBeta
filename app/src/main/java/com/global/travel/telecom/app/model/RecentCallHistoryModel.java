
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RecentCallHistoryModel {

    @SerializedName("get-subscriber-call-history-response")
    private GetSubscriberCallHistoryResponse mGetSubscriberCallHistoryResponse;

    public GetSubscriberCallHistoryResponse getGetSubscriberCallHistoryResponse() {
        return mGetSubscriberCallHistoryResponse;
    }

    public void setGetSubscriberCallHistoryResponse(GetSubscriberCallHistoryResponse getSubscriberCallHistoryResponse) {
        mGetSubscriberCallHistoryResponse = getSubscriberCallHistoryResponse;
    }

}
