
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class GetRateForPaymentPlan {

    @SerializedName("SerialNumber")
    private String serialNumber;

    @SerializedName("Rate")
    private Double rate;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
