
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetUserProfileDate {

    @SerializedName("Address")
    private String mAddress;
    @SerializedName("ContactNumber")
    private String mContactNumber;
    @SerializedName("ContactPerson")
    private String mContactPerson;
    @SerializedName("EmailID")
    private String mEmailID;
    @SerializedName("IsEmailVerified")
    private Long mIsEmailVerified;
    @SerializedName("Name")
    private String mName;
    @SerializedName("Username")
    private String mUsername;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        mContactNumber = contactNumber;
    }

    public String getContactPerson() {
        return mContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        mContactPerson = contactPerson;
    }

    public String getEmailID() {
        return mEmailID;
    }

    public void setEmailID(String emailID) {
        mEmailID = emailID;
    }

    public Long getIsEmailVerified() {
        return mIsEmailVerified;
    }

    public void setIsEmailVerified(Long isEmailVerified) {
        mIsEmailVerified = isEmailVerified;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

}

