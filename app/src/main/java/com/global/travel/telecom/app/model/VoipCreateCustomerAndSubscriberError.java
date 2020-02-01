
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class VoipCreateCustomerAndSubscriberError {

    @SerializedName("create-customer-and-subscriber-error")
    private CreateCustomerAndSubscriberError mCreateCustomerAndSubscriberError;

    public CreateCustomerAndSubscriberError getCreateCustomerAndSubscriberError() {
        return mCreateCustomerAndSubscriberError;
    }

    public void setCreateCustomerAndSubscriberError(CreateCustomerAndSubscriberError createCustomerAndSubscriberError) {
        mCreateCustomerAndSubscriberError = createCustomerAndSubscriberError;
    }

}
