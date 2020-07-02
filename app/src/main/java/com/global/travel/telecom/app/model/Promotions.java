
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Promotions {

    @SerializedName("promotion")
    private Promotion mPromotion;

    public Promotion getPromotion() {
        return mPromotion;
    }

    public void setPromotion(Promotion promotion) {
        mPromotion = promotion;
    }

}
