
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetCustomerBalanceResponse {

    @SerializedName("balance")
    private Balance mBalance;
    @SerializedName("cleared-balance")
    private ClearedBalance mClearedBalance;
    @SerializedName("pending-balance")
    private PendingBalance mPendingBalance;
    @SerializedName("trid")
    private String mTrid;

    public Balance getBalance() {
        return mBalance;
    }

    public void setBalance(Balance balance) {
        mBalance = balance;
    }

    public ClearedBalance getClearedBalance() {
        return mClearedBalance;
    }

    public void setClearedBalance(ClearedBalance clearedBalance) {
        mClearedBalance = clearedBalance;
    }

    public PendingBalance getPendingBalance() {
        return mPendingBalance;
    }

    public void setPendingBalance(PendingBalance pendingBalance) {
        mPendingBalance = pendingBalance;
    }

    public String getTrid() {
        return mTrid;
    }

    public void setTrid(String trid) {
        mTrid = trid;
    }

}
