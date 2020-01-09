
package com.global.travel.telecom.app.model;


import com.google.gson.annotations.SerializedName;

public class GetSIMStatus {

    @SerializedName("Value")
    private String mValue;

    @SerializedName("SerialNumber")
    private String mSerialNumber;

    @SerializedName("SimStatus")
    private String SimStatus;

    public String getmSerialNumber() {
        return mSerialNumber;
    }

    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }


    public String getSimStatus() {
        return SimStatus;
    }

    public void setSimStatus(String SimStatus) {
        this.SimStatus = SimStatus;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }


}
