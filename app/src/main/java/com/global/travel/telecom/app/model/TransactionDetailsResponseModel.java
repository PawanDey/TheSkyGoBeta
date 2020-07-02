
package com.global.travel.telecom.app.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TransactionDetailsResponseModel {

    @SerializedName("Table1")
    private List<TransactionDetailsActivationExtentionVoIPModel> mTransactionDetailsActivationExtentionVoIPModel;
    @SerializedName("Table2")
    private List<TransactionDetailsActivationExtentionVoIPModel> mTransactionDetailsActivationExtentionVoIPModel1;

    public List<TransactionDetailsActivationExtentionVoIPModel> getmTransactionDetailsActivationExtentionVoIPModel() {
        return mTransactionDetailsActivationExtentionVoIPModel;
    }

    public void setmTransactionDetailsActivationExtentionVoIPModel(List<TransactionDetailsActivationExtentionVoIPModel> mTransactionDetailsActivationExtentionVoIPModel) {
        this.mTransactionDetailsActivationExtentionVoIPModel = mTransactionDetailsActivationExtentionVoIPModel;
    }

    public List<TransactionDetailsActivationExtentionVoIPModel> getmTransactionDetailsActivationExtentionVoIPModel1() {
        return mTransactionDetailsActivationExtentionVoIPModel1;
    }

    public void setmTransactionDetailsActivationExtentionVoIPModel1(List<TransactionDetailsActivationExtentionVoIPModel> mTransactionDetailsActivationExtentionVoIPModel1) {
        this.mTransactionDetailsActivationExtentionVoIPModel1 = mTransactionDetailsActivationExtentionVoIPModel1;
    }
}
