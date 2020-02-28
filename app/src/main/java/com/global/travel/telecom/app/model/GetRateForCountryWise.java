
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRateForCountryWise {

    @SerializedName("RateId")
    @Expose
    private Integer rateId;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("CountryCode")
    @Expose
    private String countryCode;
    @SerializedName("Cost")
    @Expose
    private String cost;
    @SerializedName("Retail")
    @Expose
    private String retail;
    @SerializedName("IsActive")
    @Expose
    private String isActive;

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRetail() {
        return retail;
    }

    public void setRetail(String retail) {
        this.retail = retail;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}