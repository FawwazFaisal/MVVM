package com.omnisoft.retrofitpractice.Retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomRetrofitClient implements Callback {
    CustomAPI api;
    String tag;

    public CustomRetrofitClient(Call call, String tag, CustomAPI api) {
        this.tag = tag;
        this.api = api;
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call call, Response response) {
        api.onResponse(call, response, tag);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        api.onFailure(call, t, tag);
    }
}
