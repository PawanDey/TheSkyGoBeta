package com.gtt.app.model;

public enum LoginRequestTypeId {
    GOOGLE("1"),
    FACEBOOK("2"),
    LINKEDIN("3");

    private String value;

    private LoginRequestTypeId(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
