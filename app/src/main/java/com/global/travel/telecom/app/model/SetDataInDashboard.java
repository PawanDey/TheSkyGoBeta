
package com.global.travel.telecom.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class SetDataInDashboard {

    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("AccountBalance")
    @Expose
    private String accountBalance;
    @SerializedName("CurrentDate")
    @Expose
    private String currentDate;
    @SerializedName("VoIPCustomerId")
    @Expose
    private String voIPCustomerId;
    @SerializedName("VoipSipId")
    @Expose
    private String voipSipId;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("VoIPAmount")
    @Expose
    private String voIPAmount;
    @SerializedName("CLINumber")
    @Expose
    private String cLINumber;
    @SerializedName("VoipName")
    @Expose
    private String voipName;
    @SerializedName("CustomerReference")
    @Expose
    private String customerReference;
    @SerializedName("SubscriberReference")
    @Expose
    private String subscriberReference;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("Postcode")
    @Expose
    private String postcode;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleInitials")
    @Expose
    private String middleInitials;
    @SerializedName("Surname")
    @Expose
    private String surname;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("SubscriberId")
    @Expose
    private String subscriberId;
    @SerializedName("VoipStatus")
    @Expose
    private String voipStatus;
    @SerializedName("VoipUsername")
    @Expose
    private String voipUsername;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getVoIPCustomerId() {
        return voIPCustomerId;
    }

    public void setVoIPCustomerId(String voIPCustomerId) {
        this.voIPCustomerId = voIPCustomerId;
    }

    public String getVoipSipId() {
        return voipSipId;
    }

    public void setVoipSipId(String voipSipId) {
        this.voipSipId = voipSipId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getVoIPAmount() {
        return voIPAmount;
    }

    public void setVoIPAmount(String voIPAmount) {
        this.voIPAmount = voIPAmount;
    }

    public String getcLINumber() {
        return cLINumber;
    }

    public void setcLINumber(String cLINumber) {
        this.cLINumber = cLINumber;
    }

    public String getVoipName() {
        return voipName;
    }

    public void setVoipName(String voipName) {
        this.voipName = voipName;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getSubscriberReference() {
        return subscriberReference;
    }

    public void setSubscriberReference(String subscriberReference) {
        this.subscriberReference = subscriberReference;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitials() {
        return middleInitials;
    }

    public void setMiddleInitials(String middleInitials) {
        this.middleInitials = middleInitials;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getVoipStatus() {
        return voipStatus;
    }

    public void setVoipStatus(String voipStatus) {
        this.voipStatus = voipStatus;
    }

    public String getVoipUsername() {
        return voipUsername;
    }

    public void setVoipUsername(String voipUsername) {
        this.voipUsername = voipUsername;
    }

    public String getVoipPassword() {
        return voipPassword;
    }

    public void setVoipPassword(String voipPassword) {
        this.voipPassword = voipPassword;
    }

    @SerializedName("VoipPassword")
    @Expose
    private String voipPassword;
}
