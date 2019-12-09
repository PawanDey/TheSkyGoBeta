package com.global.travel.telecom.app.model;

public enum LoginRequestTypeId {
    GOOGLE("1"),
    FACEBOOK("2"),
    Email("3");

    private String value;

    private LoginRequestTypeId(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
