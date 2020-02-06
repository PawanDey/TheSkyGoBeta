package com.global.travel.telecom.app.model;

public class GetVoipRateModel {
    private String mCountryName;
    private String mPrice;
    private String mCountryCode;
    private String mRetails;

    public String getmCountryCode() {
        return mCountryCode;
    }

    public void setmCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getmRetails() {
        return mRetails;
    }

    public void setmRetails(String mRetails) {
        this.mRetails = mRetails;
    }

    public String getmCountryName() {
        return mCountryName;
    }

    public void setmCountryName(String mCountryName) {
        this.mCountryName = mCountryName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public GetVoipRateModel(String CountryCode, String CountryName, String Price, String RetailsDetails) {
        mCountryCode = CountryCode;
        mCountryName = CountryName;
        mPrice = Price;
        mRetails = RetailsDetails;
    }
}
