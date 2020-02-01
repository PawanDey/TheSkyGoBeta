
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CreateCustomerAndSubscriberResponse {

    @SerializedName("customer")
    private Customer mCustomer;
    @SerializedName("subscriber")
    private Subscriber mSubscriber;
    @SerializedName("trid")
    private String mTrid;

    public Customer getCustomer() {
        return mCustomer;
    }

    public void setCustomer(Customer customer) {
        mCustomer = customer;
    }

    public Subscriber getSubscriber() {
        return mSubscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        mSubscriber = subscriber;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
