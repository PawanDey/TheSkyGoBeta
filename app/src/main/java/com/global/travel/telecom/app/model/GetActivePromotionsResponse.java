
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetActivePromotionsResponse {

    @SerializedName("promotions")
    private Promotions mPromotions;
    @SerializedName("trid")
    private String mTrid;

    public Promotions getPromotions() {
        return mPromotions;
    }

    public void setPromotions(Promotions promotions) {
        mPromotions = promotions;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
