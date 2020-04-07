
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class PostUpdateUserProfileData {

    @SerializedName("TokenID")
    private String TokenID;
    @SerializedName("Name")
    private String Name;
    @SerializedName("EmailId")
    private String EmailId;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("HomeCountry")
    private String HomeCountry;


    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setHomeCountry(String homeCountry) {
        HomeCountry = homeCountry;
    }

}