package com.gtt.app.ui.activities;

public class notificationArrayList {
    private int mImagaeResource;
    private String mText1;
    private String mText2;
    private String mText3;

    public notificationArrayList(int imagaeResource,String text1,String text2,String text3){

        mImagaeResource=imagaeResource;
        mText1=text1;
        mText2=text2;
        mText3=text3;

    }

public int getImagaeResource(){
        return mImagaeResource;
}

    public String getmText1(){
        return mText1;
    }

    public String getmText2(){
        return mText2;
    }
    public String getmText3(){
        return mText3;
    }
}


