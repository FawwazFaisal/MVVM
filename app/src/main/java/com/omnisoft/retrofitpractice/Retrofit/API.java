package com.omnisoft.retrofitpractice.Retrofit;

import com.omnisoft.retrofitpractice.Room.Entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    public static String BASE_URL = "https://simplifiedcoding.net/demos/";

    @GET("marvel")
    Call<List<Entity>> getHeroes();
}
