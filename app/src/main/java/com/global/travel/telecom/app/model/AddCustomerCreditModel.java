
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class AddCustomerCreditModel {

    @SerializedName("apply-customer-credit-response")
    private ApplyCustomerCreditResponse mApplyCustomerCreditResponse;

    public ApplyCustomerCreditResponse getApplyCustomerCreditResponse() {
        return mApplyCustomerCreditResponse;
    }

    public void setApplyCustomerCreditResponse(ApplyCustomerCreditResponse applyCustomerCreditResponse) {
        mApplyCustomerCreditResponse = applyCustomerCreditResponse;
    }

}
