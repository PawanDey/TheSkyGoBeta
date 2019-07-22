package com.global.travel.telecom.app.presenter.implementation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddFundsResponse;
import com.global.travel.telecom.app.model.GetNotifications;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.model.UpdateFundReq;
import com.global.travel.telecom.app.presenter.interfaces.AuthenticationPresenterInterface;
import com.global.travel.telecom.app.service.APIClient;
import com.global.travel.telecom.app.ui.activities.Dashboard;
import com.global.travel.telecom.app.ui.activities.Notification;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationPresenter extends Dashboard implements AuthenticationPresenterInterface {

    public BaseView baseView;
    String Token;

    public AuthenticationPresenter(BaseActivity baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loginUser(String userEmail, LoginRequestTypeId regTypeID, String gcmToken) {
        baseView.showProgressBar();
        Log.d("mylog", "getToken;" +gcmToken);
        Call<ResponseBody> call = APIClient.getApiService().signUp(userEmail, regTypeID.getValue(), gcmToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            LoginResponse result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), LoginResponse.class);
                            baseView.onSuccess("loginUser", result);
                            baseView.hideProgressBar();

                        } else {
                            baseView.hideProgressBar();
                            baseView.onServerError("loginUser", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.hideProgressBar();
                        baseView.onServerError("loginUser", e.toString());
                    }


                } else {
                    baseView.hideProgressBar();
                    baseView.onServerError("loginUser", "");
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
        Call<ResponseBody> call = APIClient.getApiService().extensionRequest(newExtensionRequest);
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
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("ExtensionRequest", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("ExtensionRequest", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("ExtensionRequest", "Something went wrong");
                    }


                } else {
                    baseView.onServerError("activateSim", "");
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

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            AddFundsResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), AddFundsResponse.class);
                            baseView.onSuccess("AddFundsViaAPP", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("AddFundsViaAPP", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("AddFundsViaAPP", " " + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("AddFundsViaAPP", "");
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

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            AddFundsResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), AddFundsResponse.class);
                            baseView.onSuccess("UpdateFunds", result);
                            baseView.onSuccess("UpdateFunds", result);

                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("UpdateFunds", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("UpdateFunds", "" + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("UpdateFunds", "");
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
        Call<ResponseBody> call = APIClient.getApiService().activationRequest(newActivationRequest);
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
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("activateSim", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("activateSim", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("activateSim", "" + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("activateSim", "");
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

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            JSONObject table1 = (JSONObject) responseBody.getJSONArray("Table1").get(0);
                            String mValue = table1.getString("Value");
                            GetSIMStatus result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), GetSIMStatus.class);
                            if (mValue.equals("3")) {
                                GetSubscriberResponse result2 = new Gson().fromJson(responseBody.getJSONArray("Table2").get(0).toString(), GetSubscriberResponse.class);
                                baseView.onSuccess("GetSubscriber2", result2);
                            } else
                                baseView.onSuccess("GetSubscriber", result);

                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("GetSubscriber", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("GetSubscriber", "" + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("GetSubscriber", "");
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
        Call<ResponseBody> call = APIClient.getApiService().validateSim(SerialNumber, Token);
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
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("simvalidated", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("simvalidated", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("simvalidated", "" + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("simvalidated", "");
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
        Call<ResponseBody> call = APIClient.getApiService().validateMSISDN(MSISDN, Token);
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
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("rechargeMSISDN", result);
                        } else {
                            baseView.onServerError("rechargeMSISDN", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("rechargeMSISDN", "" + R.string.textSorrySomethingwentwrong);
                    }


                } else {
                    baseView.onServerError("rechargeMSISDN", "");
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


                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            Type listType = new TypeToken<List<GetNotifications>>() {
                            }.getType();
                            List<GetNotifications> result = new Gson().fromJson(responseBody.getJSONArray("Table1").toString(), listType);
                            baseView.onSuccess("notification", result);
                            baseView.hideProgressBar();

                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.hideProgressBar();
                            baseView.onServerError("notification", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.hideProgressBar();
                    }

                } else {
                    baseView.hideProgressBar();
                    baseView.onServerError("notification", "22nd");
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
    public void TranslateAPI(String inputlang, String outputlang, String text) {
        try {
            baseView.showProgressBar();
            Call<ResponseBody> call = APIClient.getTranslateApiService().TranslateAPI(inputlang, outputlang, text);
            try {

                call.enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.isSuccessful()) {
                            try {
                                ResponseBody body = response.body();
                                JSONObject responseBody = new JSONObject(body.string());

                                int respondeCode = responseBody.getInt("responseCode");
                                String respondeMessage = responseBody.getString("message");
                                if (respondeCode == 1) {

//                                    Type listType = new TypeToken<List<GetNotifications>>() {
//                                    }.getType();
//                                    List<GetNotifications> result = new Gson().fromJson(responseBody.getJSONArray("Payload").toString(), listType);

                                    String result = responseBody.getString("Payload").replace("[", "").replace("]", "").replace("\"", "");
                                    baseView.onSuccess("translateAPI", result);
                                    baseView.hideProgressBar();


                                } else {
                                    baseView.hideProgressBar();
                                    baseView.onServerError("translateAPI", respondeMessage);
                                }

                            } catch (Exception e) {
                                baseView.hideProgressBar();
                                baseView.onServerError("translateAPI", "" + R.string.textSorrySomethingwentwrong);
                            }


                        } else {
                            baseView.hideProgressBar();
                            baseView.onServerError("translateAPI", "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        baseView.hideProgressBar();
                        baseView.onFailure();
                    }
                });
            } catch (Exception e) {
                baseView.hideProgressBar();
                e.printStackTrace();
            }
        } catch (Exception e) {
//            baseView.hideProgressBar();
            e.printStackTrace();
        }
    }

}
