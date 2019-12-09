package com.global.travel.telecom.app.service;


import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.UpdateFundReq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("ValidateSubscriber")
    Call<ResponseBody> signUp(@Query("UserName") String username, @Query("RegTypeID") String reqTypeId, @Query("GCMKey") String gcmKey);

    @GET("ValidateSIM")
    Call<ResponseBody> validateSim(@Query("SerialNumber") String SerialNumber, @Query("Token") String Token);

    @GET("GetRateForPaymentPlan")
    Call<ResponseBody> GetRateForPaymentPlan(@Query("serialnumber") String SerialNumber, @Query("activationdays") int NoOfDay);

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

    @GET("ListAlertMessage")
    Call<ResponseBody> ListNotifications(@Query("TokenID") String TokenID, @Query("Condition") String Condition);

    @GET("translatorAPI.php")
    Call<ResponseBody> TranslateAPI(@Query("inputlang") String inputlang, @Query("outputlang") String outputlang, @Query("text") String text);

}