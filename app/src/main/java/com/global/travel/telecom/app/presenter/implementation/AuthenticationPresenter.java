package com.global.travel.telecom.app.presenter.implementation;

import android.os.Handler;
import android.os.Looper;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.base.BaseActivity;
import com.global.travel.telecom.app.base.BaseView;
import com.global.travel.telecom.app.model.ActivateSimResponse;
import com.global.travel.telecom.app.model.AddCustomerCreditModel;
import com.global.travel.telecom.app.model.AddFundsApp;
import com.global.travel.telecom.app.model.AddFundsResponse;
import com.global.travel.telecom.app.model.AddVoIPAPICallLogModel;
import com.global.travel.telecom.app.model.AddVoIPAPICallLogModel1;
import com.global.travel.telecom.app.model.CreateVoipCustomerSkyGo;
import com.global.travel.telecom.app.model.CurrentBalance;
import com.global.travel.telecom.app.model.GetActivePromotions;
import com.global.travel.telecom.app.model.GetNotifications;
import com.global.travel.telecom.app.model.GetRateForCountryWise;
import com.global.travel.telecom.app.model.GetRateForPaymentPlan;
import com.global.travel.telecom.app.model.GetSIMStatus;
import com.global.travel.telecom.app.model.GetSubscriberResponse;
import com.global.travel.telecom.app.model.GetUserProfileDate;
import com.global.travel.telecom.app.model.GetVoipPlans;
import com.global.travel.telecom.app.model.LoginResponse;
import com.global.travel.telecom.app.model.NewActivationRequest;
import com.global.travel.telecom.app.model.NewExtensionRequest;
import com.global.travel.telecom.app.model.PostUpdateUserProfileData;
import com.global.travel.telecom.app.model.RecentCallHistoryModel;
import com.global.travel.telecom.app.model.SetDataInDashboard;
import com.global.travel.telecom.app.model.TransactionDetailsActivationExtentionVoIPModel;
import com.global.travel.telecom.app.model.UpdateFundReq;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberError;
import com.global.travel.telecom.app.model.VoipCreateCustomerAndSubscriberGood;
import com.global.travel.telecom.app.presenter.interfaces.AuthenticationPresenterInterface;
import com.global.travel.telecom.app.service.APIClient;
import com.global.travel.telecom.app.ui.activities.Dashboard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationPresenter extends Dashboard implements AuthenticationPresenterInterface {

    public BaseView baseView;
    String requestApiName;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public AuthenticationPresenter(BaseActivity baseView) {
        this.baseView = baseView;
    }

    @Override
    public void loginUser(String Name, String Email, String Mobile, String HomeCountry, String RegTypeID, String Username, String GCMKey, int isEmailVerify) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().signUp(Name, Email, Mobile, HomeCountry, RegTypeID, Username, GCMKey, isEmailVerify);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody body = response.body();
                        int VoipCreated;
                        assert body != null;
                        JSONObject responseBody = new JSONObject(body.string());
                        JSONObject table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                        JSONObject table1 = (JSONObject) responseBody.getJSONArray("Table1").get(0);
                        int respondeCode = table.getInt("ResponseCode");
                        String respondeMessage = table.getString("ResponseMessage");
                        VoipCreated = table1.getInt("VoipCreated");
                        if (respondeCode == 0) {

                            LoginResponse result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), LoginResponse.class);
                            if (VoipCreated == 0) {
                                baseView.onSuccess("CreateVoipAccount", result);
                            }
                            mHandler.postDelayed(() -> {
                                baseView.onSuccess("loginUser", result);
                                baseView.hideProgressBar();
                            }, 2000);


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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        baseView.showProgressBar();
                        ResponseBody body = response.body();
                        assert body != null;
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
                        baseView.hideProgressBar();
                    } catch (Exception e) {
                        baseView.onServerError("UpdateFunds", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("UpdateFunds", getResources().getString(R.string.textSorrySomethingwentwrong));
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
    public void activateSim(NewActivationRequest newActivationRequest) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().activationRequest(newActivationRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
                            JSONObject table1 = (JSONObject) responseBody.getJSONArray("Table1").get(0);
                            String mValue = table1.getString("Value");
                            SetDataInDashboard setDataInDashboard = new Gson().fromJson(responseBody.getJSONArray("Table").get(0).toString(), SetDataInDashboard.class);
                            baseView.onSuccess("SetDataInDashboard", setDataInDashboard);
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
                        baseView.onServerError("GetSubscriber", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("GetSubscriber", getResources().getString(R.string.textSorrySomethingwentwrong));
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                baseView.hideProgressBar();
                baseView.onFailure();
            }
        });

    }

    @Override
    public void CreateVoipCustomerSkyGo(CreateVoipCustomerSkyGo createVoipCustomerSkyGo) {
        Call<ResponseBody> call = APIClient.getApiService().createVoipCustomerSkyGo(createVoipCustomerSkyGo);
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
                            baseView.onSuccess("CreateVoipCustomerSkyGo", respondeMessage);
                        } else {
                            baseView.onServerError("CreateVoipCustomerSkyGo", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("CreateVoipCustomerSkyGo", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("CreateVoipCustomerSkyGo", response.message());
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {


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
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        ResponseBody body = response.body();
                        assert body != null;
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
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                baseView.onFailure();
            }
        });
    }

    @Override
    public void GetVoIPRate() {
        baseView.showProgressBar();
        try {
            Call<ResponseBody> call = APIClient.getApiService().GetVoIPRate();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            ResponseBody body = response.body();
                            assert body != null;
                            JSONObject responseBody = new JSONObject(body.string());

                            Type listType = new TypeToken<List<GetRateForCountryWise>>() {
                            }.getType();
                            List<GetRateForCountryWise> result = new Gson().fromJson(responseBody.getJSONArray("Table").toString(), listType);
                            baseView.onSuccess("GetVoipRateList", result);
                        } catch (Exception e) {
                            baseView.onServerError("GetVoipRateList", e.getMessage());
                        }
                    } else {
                        baseView.onServerError("GetVoipRateList", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                    baseView.hideProgressBar();

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    baseView.onFailure();
                    baseView.hideProgressBar();
                }
            });
        } catch (Exception e) {
            baseView.hideProgressBar();
            baseView.showToast("GetVoipRate Auth Presenter :" + e.getMessage());
        }
    }

    @Override
    public void VoIPAPICall(String xmlData, String ApiName) {
        try {
            baseView.showProgressBar();
            requestApiName = ApiName;
            Call<ResponseBody> call = APIClient.getApiService().VoipApisCall(xmlData);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    String mMessage = null;
                    try {
                        mMessage = Objects.requireNonNull(response.body()).string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert mMessage != null;
                    XmlToJson jsonObject = new XmlToJson.Builder(mMessage).build();
                    switch (requestApiName) {
                        case "getCurrentBalance": {
                            CurrentBalance result = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), CurrentBalance.class);
                            baseView.onSuccess("CurrentBalance", result);
                            break;
                        }
                        case "createCustomerAndSubscriber": {
                            if (mMessage.contains("create-customer-and-subscriber-error")) {    //here is code for error form API response
                                VoipCreateCustomerAndSubscriberError result = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), VoipCreateCustomerAndSubscriberError.class);
                                baseView.onSuccess("CreateCustomerAndSubscriberError", result);
                            } else if (mMessage.contains("create-customer-and-subscriber-response")) {   //code for success reponse from VoiP api
                                VoipCreateCustomerAndSubscriberGood result = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), VoipCreateCustomerAndSubscriberGood.class);
                                baseView.onSuccess("CreateCustomerAndSubscriberGood", result);
                            }
                            break;
                        }

                        case "setSubscriberPassword": {
                            if (mMessage.contains("set-subscriber-password-response")) {   //code for success reponse from VoiP api
                                baseView.onSuccess("setSubscriberPasswordGood", "");
                            }
                            if (mMessage.contains("set-subscriber-password-error")) {
                                baseView.onSuccess("setSubscriberPasswordError", "");
                            }
                            break;
                        }

                        case "ApplyPromotion": {
                            AddVoIPAPICallLogModel result = new AddVoIPAPICallLogModel();
                            result.setAPIrequest(xmlData);
                            result.setAPIresponse(mMessage);
                            result.setAPIName(ApiName);
                            result.setPlanType("10041");
                            if (mMessage.contains("apply-promotion-response")) {
                                result.setParchaseStatus("10038");
                                baseView.onSuccess("AddVoIPAPICallLog", result);
                                baseView.onSuccess("ApplyPromotion", result);
                            } else {
                                result.setParchaseStatus("10039");
                                baseView.onSuccess("AddVoIPAPICallLog", result);
                                baseView.onServerError("ApplyPromotion", "ApplyPromotion API Error");
                            }
                            break;
                        }

                        case "getRecentCallHistory": {
                            try {
                                if (mMessage.contains("get-subscriber-call-history-response")) {
                                    RecentCallHistoryModel result = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), RecentCallHistoryModel.class);
                                    baseView.onSuccess("getRecentCallHistory", result);
                                } else if (mMessage.contains("get-subscriber-call-history-error")) {
                                    baseView.onServerError("getRecentCallHistory", "getRecentCallHistory API Error:");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }

                        case "AddCustomerCredit": {
                            AddVoIPAPICallLogModel result = new AddVoIPAPICallLogModel();
                            result.setAPIrequest(xmlData);
                            result.setAPIresponse(mMessage);
                            result.setAPIName(ApiName);
                            result.setPlanType("10040");
                            if (mMessage.contains("apply-customer-credit-response")) {
                                result.setParchaseStatus("10038");
                                baseView.onSuccess("AddVoIPAPICallLog", result);
                                AddCustomerCreditModel result1 = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), AddCustomerCreditModel.class);
                                baseView.onSuccess("AddCustomerCredit", result1);
                            } else {
                                result.setParchaseStatus("10039");
                                baseView.onSuccess("AddVoIPAPICallLog", result);
                                baseView.onServerError("AddCustomerCredit", "AddCustomerCredit API Error");
                            }
                            break;
                        }

                        case "getActivePromotion": {
                            if (mMessage.contains("get-active-promotions-response")) {
                                Type listType = new TypeToken<List<GetActivePromotions>>() {
                                }.getType();
                              try {
                                  List<GetActivePromotions> result1 = new Gson().fromJson(Objects.requireNonNull(jsonObject.toJson()).toString(), listType);
                                  baseView.onSuccess("getActivePromotion", result1);
                              }catch (Exception e){
                                  e.printStackTrace();
                              }
                            } else {
                                baseView.onServerError("getActivePromotion", "AddCustomerCredit API Error");
                            }
                            break;
                        }
                    }
                    baseView.hideProgressBar();

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    baseView.hideProgressBar();
                    baseView.showToast("VoipAPICall onFailure :" + t.getMessage());
                }
            });
        } catch (Exception e) {
            baseView.showToast("VoipAPICall Auth Presenter :" + e.getMessage());
            baseView.hideProgressBar();
            e.printStackTrace();
        }
    }

    @Override
    public void AddVoIPAPICallLog(AddVoIPAPICallLogModel1 addVoIPAPICallLogModel1) {
        try {
            Call<ResponseBody> call = APIClient.getApiService().AddVoIPAPICallLog(addVoIPAPICallLogModel1);
            try {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                ResponseBody body = response.body();
                                assert body != null;
                                JSONObject responseBody = null;
                                responseBody = new JSONObject(body.string());
                                JSONObject table = null;
                                table = (JSONObject) responseBody.getJSONArray("Table").get(0);
                                int respondeCode = table.getInt("ResponseCode");
                                String respondeMessage = table.getString("ResponseMessage");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            String error = response.errorBody().toString();
                            showToast(response.errorBody().toString());
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        baseView.onFailure();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void GetUserProfileData(String token) {
        try {
            baseView.showProgressBar();
            Call<ResponseBody> call = APIClient.getApiService().GetUserProfileData(token);
            try {
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
                                    GetUserProfileDate result = new Gson().fromJson(responseBody.getJSONArray("Table1").get(0).toString(), GetUserProfileDate.class);
                                    baseView.onSuccess("GetUserProfileData", result);
                                } else if (respondeCode == 1) {
                                    baseView.onServerError("GetUserProfileData", respondeMessage);
                                }

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                hideProgressBar();
                            }

                        } else {
                            String error = response.errorBody().toString();
                            showToast(response.errorBody().toString());
                            hideProgressBar();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        baseView.hideProgressBar();
                        baseView.onFailure();
                    }
                });
            } catch (Exception e) {
                baseView.hideProgressBar();
                e.printStackTrace();
            }
        } catch (Exception e) {
            baseView.hideProgressBar();
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateUserProfileData(PostUpdateUserProfileData postUpdateUserProfileData) {
        baseView.showProgressBar();
        Call<ResponseBody> call = APIClient.getApiService().UpdateUserProfileData(postUpdateUserProfileData);
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
                            baseView.onSuccess("UpdateUserProfileData", "Success");
                        } else {
                            baseView.onServerError("UpdateUserProfileData", respondeMessage);
                        }
                    } catch (Exception e) {
                        baseView.onServerError("UpdateUserProfileData Error :", getResources().getString(R.string.textSorrySomethingwentwrong));
                    }
                } else {
                    baseView.onServerError("UpdateUserProfileData", getResources().getString(R.string.textSorrySomethingwentwrong));
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
    public void GetAllTransaction(String token) {
        try {
            baseView.showProgressBar();
            Call<ResponseBody> call = APIClient.getApiService().GetAllTransaction(token);
            try {
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
                                    Type listType = new TypeToken<List<TransactionDetailsActivationExtentionVoIPModel>>() {
                                    }.getType();

                                    List<TransactionDetailsActivationExtentionVoIPModel> result = new Gson().fromJson(responseBody.getJSONArray("Table1").toString(), listType);
                                    List<TransactionDetailsActivationExtentionVoIPModel> result1 = new Gson().fromJson(responseBody.getJSONArray("Table2").toString(), listType);
                                    baseView.onSuccess("GetAllTransactionActivationExtension", result);
                                    baseView.onSuccess("GetAllTransactionVoIP", result1);
                                } else {
                                    baseView.onServerError("GetAllTransaction", respondeMessage);
                                }

                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                hideProgressBar();
                            }

                        } else {
                            String error = response.errorBody().toString();
                            showToast(response.errorBody().toString());
                            hideProgressBar();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        baseView.hideProgressBar();
                        baseView.onFailure();
                    }
                });
            } catch (Exception e) {
                baseView.hideProgressBar();
                e.printStackTrace();
            }
        } catch (Exception e) {
            baseView.hideProgressBar();
            e.printStackTrace();
        }
    }
}
