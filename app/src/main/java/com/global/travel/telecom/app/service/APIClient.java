package com.global.travel.telecom.app.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    private static String BASE_URL = "http://104.42.44.252/GTTAPIBeta/api/applicationuser/";
    private static String TranslateAPI_url = "http://sirrat.com/translator/";

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


}