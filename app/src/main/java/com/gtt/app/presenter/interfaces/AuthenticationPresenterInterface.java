package com.gtt.app.presenter.interfaces;

import com.gtt.app.model.AddFundsApp;
import com.gtt.app.model.LoginRequestTypeId;
import com.gtt.app.model.NewActivationRequest;
import com.gtt.app.model.NewExtensionRequest;
import com.gtt.app.model.UpdateFundReq;

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


}
