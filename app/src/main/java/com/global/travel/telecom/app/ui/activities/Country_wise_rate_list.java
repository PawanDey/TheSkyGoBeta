package com.global.travel.telecom.app.ui.activities;

public class Country_wise_rate_list {
    private String mCountryName;
    private String mPrice;

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

    public Country_wise_rate_list(String CountryName, String Price) {
        mCountryName = CountryName;
        mPrice = Price;
    }
}
