package com.global.travel.telecom.app.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    public static ApiService service;
    private static String BASE_URL = "https://api.globaltraveltelecom.com/api/applicationuser/";
    private static String BASE_URL_DataServer = "http://35.236.120.255/GTTTESTDATAAPI/api/applicationuser/";

    private static String TranslateAPI_url = "http://sirrat.com/translator/";
    //    public static String BACKEND_URL = "https://www.sirrat.com/Stripe/";  //live  url for stripe
    public static String BACKEND_URL = "https://www.sirrat.com/Stripe_test/";  //test url for stripe

    public static ApiService getApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
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

    public static ApiService getApiServiceDataServer() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_DataServer)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(ApiService.class);
        return retrofit.create(ApiService.class);
    }
}