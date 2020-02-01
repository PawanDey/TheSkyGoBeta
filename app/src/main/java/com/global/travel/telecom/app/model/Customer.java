
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Customer {

    @SerializedName("customer-reference")
    private String mCustomerReference;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("status")
    private String mStatus;

    public String getCustomerReference() {
        return mCustomerReference;
    }

    public void setCustomerReference(String customerReference) {
        mCustomerReference = customerReference;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
