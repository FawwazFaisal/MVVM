package com.omnisoft.retrofitpractice.Retrofit;

import retrofit2.Call;
import retrofit2.Response;

public interface CustomAPI {
    void onResponse(Call call, Response response, String tag);

    void onFailure(Call call, Throwable t, String tag);
}
