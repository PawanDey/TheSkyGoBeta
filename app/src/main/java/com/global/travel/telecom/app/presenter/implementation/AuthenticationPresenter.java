package com.global.travel.telecom.app.presenter.implementation;

import android.util.Log;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddFundsResponse;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.model.GetNotifications;
import com.global.travel.telecom.app.model.GetRateForPaymentPlan;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.model.GetVoipPlans;
import com.global.travel.telecom.app.model.LoginRequestTypeId;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.UpdateFundReq;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberError;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberGood;
import com.global.travel.telecom.app.presenter.interfaces.AuthenticationPresenterInterface;
import com.global.travel.telecom.app.service.APIClient;
import com.global.travel.telecom.app.ui.activities.Dashboard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationPresenter extends Dashboard implements AuthenticationPresenterInterface {

    public BaseView baseView;
    String Token;
    String requestApiName;

    public AuthenticationPresenter(BaseActivity baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loginUser(String userEmail, LoginRequestTypeId regTypeID, String gcmToken) {
        baseView.showProgressBar();
        Log.d("mylog", "getToken:" + gcmToken);
        Call<ResponseBody> call = APIClient.getApiService().signUp(userEmail, regTypeID.getValue(), gcmToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody body = response.body();
                        int dealerID = 0;
                        JSONObject responseBody = new JSONObject(body.string());
                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        JSONObject table1 = (JSONObject) responseBody.getJSONArray("Table1").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        try {
                            dealerID = table1.getInt("UserID");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (respondeCode == 0) {
                            LoginResponse result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), LoginResponse.class);
                            baseView.onSuccess("loginUser", result);
                            baseView.hideProgressBar();
                            if (dealerID > 0) {
                                baseView.onSuccess("CreateVoipAccount", result);
                            }

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
                    baseView.onServerError("loginUser", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                            baseView.onServerError("ActivationExtensionRequest", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("ActivationExtensionRequest", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("ActivationExtensionRequest", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                        baseView.onServerError("AddFundsViaAPP", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("AddFundsViaAPP", getResources().getString(R.string.textSorrySomethingwentwrong));
                }
                try {
                    baseView.hideProgressBar();
                } catch (Exception e) {
                    e.printStackTrace();
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
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("UpdateFunds", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("UpdateFunds", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("UpdateFunds", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                            String MSISDN = table.getString("MSISDN");
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("activateSim", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("ActivationExtensionRequest", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("ActivationExtensionRequest", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("ActivationExtensionRequest", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                                String mSimStatus = table1.getString("SimStatus");
                                if (mSimStatus.contains("Active")) {
                                    GetSubscriberResponse result2 = new Gson().fromJson(responseBody.getJSONArray("Table2").get(0).toString(), GetSubscriberResponse.class);
                                    baseView.onSuccess("GetSubscriber2", result2);
                                } else if (mSimStatus.contains("Suspension Request") || mSimStatus.contains("Used")) {
                                    baseView.onSuccess("GetSubscriber3", "");
                                } else {
                                    showToast("Error in GetSubscriber: value 3 ");
                                }
                            } else
                                baseView.onSuccess("GetSubscriber", result);
                        } else if (respondeCode == 1 || respondeCode == 3) {
                            baseView.onServerError("GetSubscriber", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("GetSubscriber", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("GetSubscriber", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                        baseView.onServerError("simvalidated", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("simvalidated", getResources().getString(R.string.textSorrySomethingwentwrong));
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
    public void GetRateForPaymentPlan(String SerialNumber, int NoOfDay, int type, String MSISDN) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().GetRateForPaymentPlan(SerialNumber, NoOfDay, type, MSISDN);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            GetRateForPaymentPlan result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), GetRateForPaymentPlan.class);
                            baseView.onSuccess("GetRateForPaymentPlan_SpecialPlan", result);
                        } else if (respondeCode == 1) {
                            GetRateForPaymentPlan result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), GetRateForPaymentPlan.class);
                            baseView.onSuccess("GetRateForPaymentPlan_endUser", result);
                        } else {
                            baseView.onServerError("GetRateForPaymentPlan", respondeMessage);
                        }

                    } catch (Exception e) {
                        baseView.onServerError("GetRateForPaymentPlan", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }


                } else {
                    baseView.onServerError("GetRateForPaymentPlan", getResources().getString(R.string.textSorrySomethingwentwrong));
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, Response<ResponseBody> response) {

                baseView.hideProgressBar();

                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        assert body != null;
                        JSONObject responseBody = new JSONObject(body.string());

                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        if (respondeCode == 0) {
                            ActivateSimResponse result = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), ActivateSimResponse.class);
                            baseView.onSuccess("rechargeMSISDN", result);
                        } else if (respondeCode == 1) {
                            baseView.onServerError("rechargeMSISDN", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("rechargeMSISDN", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }


                } else {
                    baseView.onServerError("rechargeMSISDN", getResources().getString(R.string.textSorrySomethingwentwrong));
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {


                if (response.isSuccessful()) {

                    try {

                        ResponseBody body = response.body();
                        assert body != null;
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
                    baseView.onServerError("notification", getResources().getString(R.string.textSorrySomethingwentwrong));
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
                    public void onResponse(@NotNull Call<ResponseBody> call, Response<ResponseBody> response) {


                        if (response.isSuccessful()) {
                            try {
                                ResponseBody body = response.body();
                                baseView.onSuccess("translateAPI", body);
                            } catch (Exception e) {
                                baseView.hideProgressBar();
                                baseView.onServerError("translateAPI", getResources().getString(R.string.textSorrySomethingwentwrong));
                            }
                        } else {
                            baseView.hideProgressBar();
                            baseView.onServerError("translateAPI", getResources().getString(R.string.textSorrySomethingwentwrong));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
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

    @Override
    public void GetVoipPlan() {
        Call<ResponseBody> call = APIClient.getApiService().GetVoipPlan();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        ResponseBody body = response.body();
                        JSONObject responseBody = new JSONObject(body.string());

                        Type listType = new TypeToken<List<GetVoipPlans>>() {
                        }.getType();
                        List<GetVoipPlans> result = new Gson().fromJson(responseBody.getJSONArray("Table").toString(), listType);
                        baseView.onSuccess("GetVoipPlanList", result);
                    } catch (Exception e) {
                        baseView.onServerError("GetVoipPlanList", e.getMessage());
                    }
                } else {
                    baseView.onServerError("GetVoipPlanList", getResources().getString(R.string.textSorrySomethingwentwrong));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseView.onFailure();
            }
        });
    }

    @Override
    public void VoIPAPICall(String xmlData, String ApiName) {
        try {
            requestApiName = ApiName;
            OkHttpClient client = new OkHttpClient();
            Request request = APIClient.getVoiPService(xmlData);
            try {

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {

                        switch (requestApiName) {

                            case "getCurrentBalance": {
                                showToast("getCurrentBalance API Error:" + e.getMessage());
                                break;
                            }
                            case "createCustomerAndSubscriber": {
                                showToast("createCustomerAndSubscriber API Error:" + e.getMessage());
                                break;
                            }
                            case "setSubscriberPassword": {
                                showToast("setSubscriberPassword API Error:" + e.getMessage());
                                break;
                            }
                            case "ApplyPromotion": {
                                showToast("ApplyPromotion API Error:" + e.getMessage());
                                break;
                            }

                        }
                    }

                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        try {
//                            ResponseBody responseBody = response.body();
                            String mMessage = response.body().string();
                            XmlToJson jsonObject = new XmlToJson.Builder(mMessage).build();
                            switch (requestApiName) {
                                case "getCurrentBalance": {
                                    CurrentBalance result = new Gson().fromJson(jsonObject.toJson().toString(), CurrentBalance.class);
                                    baseView.onSuccess("CurrentBalance", result);
                                    break;
                                }
                                case "createCustomerAndSubscriber": {
                                    try {
                                        if (mMessage.contains("create-customer-and-subscriber-error")) {    //here is code for error form API response
                                            VoipCreateCustomerAndSubscriberError result = new Gson().fromJson(jsonObject.toJson().toString(), VoipCreateCustomerAndSubscriberError.class);
                                            baseView.onSuccess("CreateCustomerAndSubscriberError", result);
                                        } else if (mMessage.contains("create-customer-and-subscriber-response")) {   //code for success reponse from VoiP api
                                            VoipCreateCustomerAndSubscriberGood result = new Gson().fromJson(jsonObject.toJson().toString(), VoipCreateCustomerAndSubscriberGood.class);
                                            baseView.onSuccess("CreateCustomerAndSubscriberGood", result);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showToast("VoIP Authentication Error: " + e.getMessage());
                                    }

                                    break;
                                }
                                case "setSubscriberPassword": {
                                    try {
                                        if (mMessage.contains("set-subscriber-password-response")) {   //code for success reponse from VoiP api
                                            baseView.onSuccess("setSubscriberPasswordGood", "");
                                        }
                                        if (mMessage.contains("set-subscriber-password-error")) {
                                            showToast("setSubscriberPassword Error:" + mMessage);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showToast("VoIP Authentication Error: " + e.getMessage());
                                    }
                                    break;
                                }
                                case "ApplyPromotion": {
                                    if (mMessage.contains("apply-promotion-response")) {   //code for success reponse from VoiP api
                                        baseView.onSuccess("ApplyPromotion", mMessage);
                                    }
                                    if (mMessage.contains("apply-promotion-error")) {
                                        baseView.onServerError("ApplyPromotion", "ApplyPromotion API Error:");
                                    }
                                    break;
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });


            } catch (Exception e) {
                baseView.hideProgressBar();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
