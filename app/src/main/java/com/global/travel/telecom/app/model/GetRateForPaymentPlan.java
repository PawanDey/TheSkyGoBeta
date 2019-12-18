
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class GetRateForPaymentPlan {

    @SerializedName("SerialNumber")
    private String serialNumber;

    @SerializedName("MSISDN")
    private String MSISDN;

    @SerializedName("type")
    private int type;

    @SerializedName("Rate")
    private Double rate;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
