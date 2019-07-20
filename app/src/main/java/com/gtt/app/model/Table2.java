
package com.gtt.app.model;

import com.google.gson.annotations.SerializedName;


public class Table2 {

    @SerializedName("AlertCount")
    private Long mAlertCount;

    public Long getAlertCount() {
        return mAlertCount;
    }

    public void setAlertCount(Long alertCount) {
        mAlertCount = alertCount;
    }

}
