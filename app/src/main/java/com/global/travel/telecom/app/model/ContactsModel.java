package com.global.travel.telecom.app.model;


class ContactsModel {
    public String name;
    public String mobileNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ContactsModel(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
    }
}
