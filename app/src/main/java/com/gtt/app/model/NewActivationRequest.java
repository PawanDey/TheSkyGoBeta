
package com.gtt.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class NewActivationRequest {

    @SerializedName("AmountCharged")
    private String mAmountCharged;
    @SerializedName("NumberOfDays")
    private String mNumberOfDays;
    @SerializedName("RefNo")
    private String mRefNo;
    @SerializedName("RequestedDevice")
    private String mRequestedDevice;
    @SerializedName("RequestedForDtTm")
    private String mRequestedForDtTm;
    @SerializedName("RequestedIP")
    private String mRequestedIP;
    @SerializedName("RequestedOS")
    private String mRequestedOS;
    @SerializedName("SerialNumber")
    private String mSerialNumber;
    @SerializedName("Token")
    private String mToken;

    public String getAmountCharged() {
        return mAmountCharged;
    }

    public void setAmountCharged(String amountCharged) {
        mAmountCharged = amountCharged;
    }

    public String getNumberOfDays() {
        return mNumberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        mNumberOfDays = numberOfDays;
    }

    public String getRefNo() {
        return mRefNo;
    }

    public void setRefNo(String refNo) {
        mRefNo = refNo;
    }

    public String getRequestedDevice() {
        return mRequestedDevice;
    }

    public void setRequestedDevice(String requestedDevice) {
        mRequestedDevice = requestedDevice;
    }

    public String getRequestedForDtTm() {
        return mRequestedForDtTm;
    }

    public void setRequestedForDtTm(String requestedForDtTm) {
        mRequestedForDtTm = requestedForDtTm;
    }

    public String getRequestedIP() {
        return mRequestedIP;
    }

    public void setRequestedIP(String requestedIP) {
        mRequestedIP = requestedIP;
    }

    public String getRequestedOS() {
        return mRequestedOS;
    }

    public void setRequestedOS(String requestedOS) {
        mRequestedOS = requestedOS;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        mSerialNumber = serialNumber;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

}
