package com.omnisoft.retrofitpractice.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomRetrofitClient {

    public static void enqueue(Call call, String tag, CustomAPI api) {

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                api.onResponse(call, response, tag);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                api.onFailure(call, t, tag);
            }
        });
    }
}
