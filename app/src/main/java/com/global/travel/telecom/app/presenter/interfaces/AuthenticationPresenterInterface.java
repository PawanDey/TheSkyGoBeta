package com.global.travel.telecom.app.presenter.interfaces;

import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddVoIPAPICallLogModel1;
import com.global.travel.telecom.app.model.CreateVoipCustomerSkyGo;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.PostUpdateUserProfileData;
import com.global.travel.telecom.app.model.UpdateFundReq;


public interface AuthenticationPresenterInterface {
    void loginUser(String Name, String Email, String Mobile, String HomeCountry, String RegTypeID, String Username, String GCMKey, int isEmailVerify);

    void activateSim(NewActivationRequest newActivationRequest);

    void AddFundsAPI(AddFundsApp addFundsApp);

    void validateSim(String SerialNumber, String Token);

    void GetRateForPaymentPlan(String SerialNumber, int NoOfDay, int type, String MSISDN);

    void validateMSISDN(String MSISDN, String Token);

    void GetSubscriber(String Token);

    void UpdateFundsMethod(UpdateFundReq updateFundReq);

    void extensionRequest(NewExtensionRequest newExtensionRequest);

    void ListNotifications(String TokenID, String Condition);

    void CreateVoipCustomerSkyGo(CreateVoipCustomerSkyGo createVoipCustomerSkyGo);

    void TranslateAPI(String inputlang, String outputlang, String text);

    void VoIPAPICall(String xmlData, String ApiName);

    void GetVoipPlan();

    void GetVoIPRate();

    void AddVoIPAPICallLog(AddVoIPAPICallLogModel1 addVoIPAPICallLogModel1);

    void GetUserProfileData(String token);

    void UpdateUserProfileData(PostUpdateUserProfileData postUpdateUserProfileData);

    void GetAllTransaction(String token);
}
