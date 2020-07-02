
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetActivePromotions {

    @SerializedName("get-active-promotions-response")
    private GetActivePromotionsResponse mGetActivePromotionsResponse;

    public GetActivePromotionsResponse getGetActivePromotionsResponse() {
        return mGetActivePromotionsResponse;
    }

    public void setGetActivePromotionsResponse(GetActivePromotionsResponse getActivePromotionsResponse) {
        mGetActivePromotionsResponse = getActivePromotionsResponse;
    }

}
