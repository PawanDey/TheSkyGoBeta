
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VoipCreateCustomerAndSubscriberGood {

    @SerializedName("create-customer-and-subscriber-response")
    private CreateCustomerAndSubscriberResponse mCreateCustomerAndSubscriberResponse;

    public CreateCustomerAndSubscriberResponse getCreateCustomerAndSubscriberResponse() {
        return mCreateCustomerAndSubscriberResponse;
    }

    public void setCreateCustomerAndSubscriberResponse(CreateCustomerAndSubscriberResponse createCustomerAndSubscriberResponse) {
        mCreateCustomerAndSubscriberResponse = createCustomerAndSubscriberResponse;
    }

}
