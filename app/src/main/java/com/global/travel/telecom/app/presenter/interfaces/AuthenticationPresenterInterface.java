package com.global.travel.telecom.app.presenter.interfaces;

import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.UpdateFundReq;

public interface AuthenticationPresenterInterface {

    public void loginUser(String userEmail, LoginRequestTypeId regTypeID, String gcmToken );
    public void activateSim(NewActivationRequest newActivationRequest );
    public void AddFundsAPI(AddFundsApp addFundsApp);
    public void validateSim(String SerialNumber, String Token);
    public void validateMSISDN(String MSISDN, String Token);
    public  void GetSubscriber(String Token);
    public void  UpdateFundsMethod(UpdateFundReq updateFundReq);
    public void extensionRequest(NewExtensionRequest newExtensionRequest);
    public void ListNotifications(String TokenID, String Condition);
    public void TranslateAPI(String inputlang, String outputlang, String text);


}
