
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Promotion {

    @SerializedName("active-offers")
    private ActiveOffers mActiveOffers;
    @SerializedName("moniker")
    private String mMoniker;

    public ActiveOffers getActiveOffers() {
        return mActiveOffers;
    }

    public void setActiveOffers(ActiveOffers activeOffers) {
        mActiveOffers = activeOffers;
    }

    public String getMoniker() {
        return mMoniker;
    }

    public void setMoniker(String moniker) {
        mMoniker = moniker;
    }

}
