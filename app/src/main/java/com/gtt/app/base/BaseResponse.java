
package com.gtt.app.base;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BaseResponse<T> {

    @SerializedName("able")
    private List<Table> mTable;
    @SerializedName("Table1")
    private List<T> mTable1;

    public List<Table> getTable() {
        return mTable;
    }

    public void setTable(List<Table> table) {
        mTable = table;
    }

    public List<T> getTable1() {
        return mTable1;
    }

    public void setTable1(List<T> table1) {
        mTable1 = table1;
    }

}
