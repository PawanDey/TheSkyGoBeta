
package com.gtt.app.model;


import com.google.gson.annotations.SerializedName;

public class GetSIMStatus {

    @SerializedName("Value")
    private String mValue;

    @SerializedName("SerialNumber")
    private String mSerialNumber;


    public String getmSerialNumber() {
        return mSerialNumber;
    }

    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }


    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }


}
