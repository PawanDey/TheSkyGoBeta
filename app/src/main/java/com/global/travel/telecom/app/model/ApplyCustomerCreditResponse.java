
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class ApplyCustomerCreditResponse {

    @SerializedName("account-transaction-id")
    private String mAccountTransactionId;
    @SerializedName("balance")
    private Balance mBalance;
    @SerializedName("trid")
    private String mTrid;

    public String getAccountTransactionId() {
        return mAccountTransactionId;
    }

    public void setAccountTransactionId(String accountTransactionId) {
        mAccountTransactionId = accountTransactionId;
    }

    public Balance getBalance() {
        return mBalance;
    }

    public void setBalance(Balance balance) {
        mBalance = balance;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
