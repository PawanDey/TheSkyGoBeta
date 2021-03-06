package com.global.travel.telecom.app.service;


import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddVoIPAPICallLogModel1;
import com.global.travel.telecom.app.model.CreateVoipCustomerSkyGo;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.PostUpdateUserProfileData;
import com.global.travel.telecom.app.model.UpdateFundReq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    //    @GET("ValidateSubscriber")
    @GET("TestValidateSubscriber")
    Call<ResponseBody> signUp(@Query("Name") String Name, @Query("Email") String Email, @Query("Mobile") String Mobile, @Query("HomeCountry") String HomeCountry, @Query("RegTypeID") String RegTypeID, @Query("Username") String Username, @Query("GCMKey") String GCMKey, @Query("isEmailVerify") int isEmailVerify);

    @GET("ValidateSIM")
    Call<ResponseBody> validateSim(@Query("SerialNumber") String SerialNumber, @Query("Token") String Token);

    @GET("GetRateForPaymentPlan")
    Call<ResponseBody> GetRateForPaymentPlan(@Query("serialnumber") String SerialNumber, @Query("activationdays") int NoOfDay, @Query("type") int type, @Query("MSISDN") String MSISDN);

    @GET("GetSubscriberDetails")
    Call<ResponseBody> GetSubscriber(@Query("TokenID") String TokenID);

    @GET("validateMSISDN")
    Call<ResponseBody> validateMSISDN(@Query("MSISDN") String MSISDN, @Query("Token") String Token);

    @GET("ListICCID")
    Call<ResponseBody> AllSIM(@Query("PurchaseId") String PurchaseId, @Query("NetworkId") String NetworkId, @Query("DealerId") String DealerId, @Query("ICCIDStatus") String ICCIDStatus
            , @Query("TokenID") String TokenID, @Query("SIMID") String SIMID);

    @POST("NewActivationRequest")
    Call<ResponseBody> activationRequest(@Body NewActivationRequest newActivationRequest);

    @POST("AddfundsViaApp")
    Call<ResponseBody> AddFundsViaAPP(@Body AddFundsApp addFundsApp);

    @POST("UpdateFunds")
    Call<ResponseBody> UpdateFundsMethod(@Body UpdateFundReq updateFundReq);

    @POST("NewExtensionRequest")
    Call<ResponseBody> extensionRequest(@Body NewExtensionRequest newExtensionRequest);

    @POST("SpinsertVoIPCustomer")
    Call<ResponseBody> createVoipCustomerSkyGo(@Body CreateVoipCustomerSkyGo createVoipCustomerSkyGo);

    @GET("ListAlertMessage")
    Call<ResponseBody> ListNotifications(@Query("TokenID") String TokenID, @Query("Condition") String Condition);

    @GET("translatorAPI.php")
    Call<ResponseBody> TranslateAPI(@Query("inputlang") String inputlang, @Query("outputlang") String outputlang, @Query("text") String text);

    @GET("GetVoIPPlans")
    Call<ResponseBody> GetVoipPlan();

    @GET("GetVoIPRate")
    Call<ResponseBody> GetVoIPRate();

    @GET("XmlData")
    Call<ResponseBody> VoipApisCall(@Query("requestXml") String xmlQueryString);

    @POST("AddVoIPAPICallLog")
    Call<ResponseBody> AddVoIPAPICallLog(@Body AddVoIPAPICallLogModel1 addVoIPAPICallLogModel1);

    @GET("GetUserProfileData")
    Call<ResponseBody> GetUserProfileData(@Query("TokenId") String token);

    @POST("UpdateUserProfileData")
    Call<ResponseBody> UpdateUserProfileData(@Body PostUpdateUserProfileData postUpdateUserProfileData);

    @GET("GetAllTransaction")
    Call<ResponseBody> GetAllTransaction(@Query("TokenId") String token);
}
