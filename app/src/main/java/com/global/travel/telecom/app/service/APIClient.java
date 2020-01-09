package com.global.travel.telecom.app.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    //    private static String BASE_URL = "http://104.42.44.252/GTTAPIBeta/api/applicationuser/";
//    private static String BASE_URL = "https://api.globaltraveltelecom.com/api/applicationuser/";
    private static String BASE_URL = "http://35.236.120.255/apitest.globaltraveltelecom.com/api/applicationuser/";
    private static String TranslateAPI_url = "http://sirrat.com/translator/";
    private static String VoIP_url = "https://api.s.im/pip/api/execute.mth/";

    public static Retrofit getClient() {

//        if (retrofit == null) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
//        }


        return retrofit;
    }

    public static ApiService service;

    public static ApiService getApiService() {
//        if (retrofit == null) {
        getClient();
//        }

        service = retrofit.create(ApiService.class);
        return retrofit.create(ApiService.class);
    }

    public static ApiService getTranslateApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(TranslateAPI_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(ApiService.class);
        return retrofit.create(ApiService.class);
    }

    public static Request getVoiPService(String requestBody) {
        MediaType mediaType = MediaType.parse("application/xml");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(VoIP_url)
                .post(body)
                .addHeader("content-type", "application/xml")
                .addHeader("cache-control", "no-cache")
                .build();
        return request;


    }


}