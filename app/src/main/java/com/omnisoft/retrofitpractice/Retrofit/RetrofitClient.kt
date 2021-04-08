package com.omnisoft.retrofitpractice.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private final API api;

    private RetrofitClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build();
        api = retrofit.create(API.class);
    }

    public static RetrofitClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new RetrofitClient(baseUrl);
        }
        return instance;
    }

    public API getApi() {
        return api;
    }
}
