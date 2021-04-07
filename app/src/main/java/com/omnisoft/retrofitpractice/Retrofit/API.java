package com.omnisoft.retrofitpractice.Retrofit;

import com.omnisoft.retrofitpractice.FCM.FCMRequestBody;
import com.omnisoft.retrofitpractice.FCM.FCMResponseBody;
import com.omnisoft.retrofitpractice.Room.Entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    @GET("marvel")
    Call<List<Entity>> getHeroes();

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEs0vHy0:APA91bEo8VmqG-6H2yvGzAxjQaFqmzbRunBmnZZgWcouDnD4BuB082djlGEdJYPWn2o2nCEamcLK25Z5glZCnDhp9R0gmrXv8fjGRJbeWrEHYV6FbIgi8IyH03t20Q623kLBf2QYIc5Q"
            }
    )
    @POST("fcm/send")
    Call<FCMResponseBody> sendNotification(@Body FCMRequestBody body);
}
