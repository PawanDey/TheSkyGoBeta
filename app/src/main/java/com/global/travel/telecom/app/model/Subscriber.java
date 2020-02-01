
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Subscriber {

    @SerializedName("auto-cli")
    private String mAutoCli;
    @SerializedName("block-gprs-data")
    private String mBlockGprsData;
    @SerializedName("first-activity")
    private String mFirstActivity;
    @SerializedName("id")
    private String mId;
    @SerializedName("last-activity")
    private String mLastActivity;
    @SerializedName("name")
    private String mName;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("subscriber-reference")
    private String mSubscriberReference;
    @SerializedName("voicemail-enabled")
    private String mVoicemailEnabled;
    @SerializedName("withhold-cli")
    private String mWithholdCli;

    public String getAutoCli() {
        return mAutoCli;
    }

    public void setAutoCli(String autoCli) {
        mAutoCli = autoCli;
    }

    public String getBlockGprsData() {
        return mBlockGprsData;
    }

    public void setBlockGprsData(String blockGprsData) {
        mBlockGprsData = blockGprsData;
    }

    public String getFirstActivity() {
        return mFirstActivity;
    }

    public void setFirstActivity(String firstActivity) {
        mFirstActivity = firstActivity;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLastActivity() {
        return mLastActivity;
    }

    public void setLastActivity(String lastActivity) {
        mLastActivity = lastActivity;
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

    public String getSubscriberReference() {
        return mSubscriberReference;
    }

    public void setSubscriberReference(String subscriberReference) {
        mSubscriberReference = subscriberReference;
    }

    public String getVoicemailEnabled() {
        return mVoicemailEnabled;
    }

    public void setVoicemailEnabled(String voicemailEnabled) {
        mVoicemailEnabled = voicemailEnabled;
    }

    public String getWithholdCli() {
        return mWithholdCli;
    }

    public void setWithholdCli(String withholdCli) {
        mWithholdCli = withholdCli;
    }

}
