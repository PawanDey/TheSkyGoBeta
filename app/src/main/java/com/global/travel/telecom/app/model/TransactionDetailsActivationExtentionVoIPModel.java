
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

public class TransactionDetailsActivationExtentionVoIPModel {
    @SerializedName("Amount")
    private Double mAmount;
    @SerializedName("PaymentDtTm")
    private String mPaymentDtTm;
    @SerializedName("PaymentStatus")
    private String mPaymentStatus;
    @SerializedName("PaymentType")
    private String mPaymentType;
    @SerializedName("Remarks")
    private String mRemarks;
    @SerializedName("TxnRefNo")
    private String mTxnRefNo;

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }

    public String getPaymentDtTm() {
        return mPaymentDtTm;
    }

    public void setPaymentDtTm(String paymentDtTm) {
        mPaymentDtTm = paymentDtTm;
    }

    public String getPaymentStatus() {
        return mPaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        mPaymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return mPaymentType;
    }

    public void setPaymentType(String paymentType) {
        mPaymentType = paymentType;
    }

    public String getRemarks() {
        return mRemarks;
    }

    public void setRemarks(String remarks) {
        mRemarks = remarks;
    }

    public String getTxnRefNo() {
        return mTxnRefNo;
    }

    public void setTxnRefNo(String txnRefNo) {
        mTxnRefNo = txnRefNo;
    }

}
