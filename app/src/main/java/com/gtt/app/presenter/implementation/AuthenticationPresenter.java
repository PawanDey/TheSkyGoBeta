package com.gtt.app.presenter.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gtt.app.base.BaseView;
import com.gtt.app.model.ActivateSimResponse;
import com.gtt.app.model.AddFundsApp;
import com.gtt.app.model.AddFundsResponse;
import com.gtt.app.model.GetNotifications;
import com.gtt.app.model.GetSubscriberResponse;
import com.gtt.app.model.LoginRequestTypeId;
import com.gtt.app.model.LoginResponse;
import com.gtt.app.model.NewActivationRequest;
import com.gtt.app.model.NewExtensionRequest;
import com.gtt.app.model.GetSIMStatus;
import com.gtt.app.model.UpdateFundReq;
import com.gtt.app.presenter.interfaces.AuthenticationPresenterInterface;
import com.gtt.app.service.APIClient;
import com.gtt.app.ui.activities.LoginActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationPresenter implements AuthenticationPresenterInterface {

    public BaseView baseView;
    String Token;

    public AuthenticationPresenter(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loginUser(String userEmail, LoginRequestTypeId regTypeID, String gcmToken) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().signUp( userEmail, regTypeID.getValue(), gcmToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            LoginResponse result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), LoginResponse.class );
                            baseView.onSuccess( "loginUser" , result );
                        } else {
                            baseView.onServerError( "loginUser" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "loginUser" ,e.toString() );
                    }



                } else {
                    baseView.onServerError( "loginUser" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });
    }


    @Override
    public void extensionRequest(NewExtensionRequest newExtensionRequest) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().extensionRequest(newExtensionRequest );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class );
                            baseView.onSuccess( "ExtensionRequest" , result );
                        } else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "ExtensionRequest" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "ExtensionRequest" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "activateSim" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }

    @Override
    public void AddFundsAPI(AddFundsApp addFundsApp) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().AddFundsViaAPP(addFundsApp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            AddFundsResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), AddFundsResponse.class );
                            baseView.onSuccess( "AddFundsViaAPP" , result );
                        } else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "AddFundsViaAPP" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "AddFundsViaAPP" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "AddFundsViaAPP" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }



    @Override
    public void UpdateFundsMethod(UpdateFundReq updateFundReq) {
    baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().UpdateFundsMethod(updateFundReq);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            AddFundsResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), AddFundsResponse.class );                            baseView.onSuccess( "UpdateFunds" , result );
                        } else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "UpdateFunds" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "UpdateFunds" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "UpdateFunds" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }

    @Override
    public void activateSim(NewActivationRequest newActivationRequest) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().activationRequest( newActivationRequest );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class );
                            baseView.onSuccess( "activateSim" , result );
                        } else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "activateSim" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "activateSim" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "activateSim" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }

    @Override
    public void GetSubscriber(String Token) {
//        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().GetSubscriber(Token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

//                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            JSONObject table1 = (JSONObject) responseBody.getJSONArray("Table1").get(0);
                            String mValue = table1.getString("Value");
                            GetSIMStatus result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), GetSIMStatus.class);
                            if(mValue.equals("3"))
                            {
                                GetSubscriberResponse result2 = new Gson().fromJson(responseBody.getJSONArray("Table2").get(0).toString(), GetSubscriberResponse.class);
                                baseView.onSuccess( "GetSubscriber2" , result2 );
                            }
                            else
                                baseView.onSuccess( "GetSubscriber" , result );

                        }
                        else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "GetSubscriber" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "GetSubscriber" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "GetSubscriber" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });
    }

    @Override
    public void validateSim(String SerialNumber, String Token) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().validateSim( SerialNumber,Token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class );
                            baseView.onSuccess( "simvalidated" , result );
                        } else if (respondeCode==1||respondeCode==3)
                        {
                            baseView.onServerError( "simvalidated" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "simvalidated" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "simvalidated" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });
    }

    @Override
    public void validateMSISDN(String MSISDN, String Token) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().validateMSISDN(MSISDN,Token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if( response.isSuccessful() ){

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if( respondeCode == 0 ){
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class );
                            baseView.onSuccess( "rechargeMSISDN" , result );
                        } else {
                            baseView.onServerError( "rechargeMSISDN" , respondeMessage );
                        }

                    } catch (Exception e) {
                        baseView.onServerError( "rechargeMSISDN" , "Something went wrong" );
                    }



                } else {
                    baseView.onServerError( "rechargeMSISDN" , "" );
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });
    }

    @Override
    public void ListNotifications(String TokenID, String Condition) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().ListNotifications(TokenID, Condition);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            Type listType = new TypeToken<List<GetNotifications>>() {}.getType();
                            List<GetNotifications> result = new Gson().fromJson(responseBody.getJSONArray("Table1").toString(), listType);
                            baseView.onSuccess("notification", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("notification", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("notification", "Something went wrong");
                    }

                } else {
                    baseView.onServerError("notification", "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }


}
