
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActiveOffers {

    @SerializedName("offer")
    private Offer mOffer;

    public Offer getOffer() {
        return mOffer;
    }

    public void setOffer(Offer offer) {
        mOffer = offer;
    }

}
