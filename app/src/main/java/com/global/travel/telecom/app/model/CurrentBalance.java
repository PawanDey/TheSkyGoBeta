
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CurrentBalance {

    @SerializedName("get-customer-balance-response")
    private GetCustomerBalanceResponse mGetCustomerBalanceResponse;

    public GetCustomerBalanceResponse getGetCustomerBalanceResponse() {
        return mGetCustomerBalanceResponse;
    }

    public void setGetCustomerBalanceResponse(GetCustomerBalanceResponse getCustomerBalanceResponse) {
        mGetCustomerBalanceResponse = getCustomerBalanceResponse;
    }

}
