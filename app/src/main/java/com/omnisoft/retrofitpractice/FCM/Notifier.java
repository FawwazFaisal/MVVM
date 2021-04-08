package com.omnisoft.retrofitpractice.FCM;

import com.omnisoft.retrofitpractice.Retrofit.CustomAPI;
import com.omnisoft.retrofitpractice.Retrofit.CustomRetrofitClient;
import com.omnisoft.retrofitpractice.Retrofit.RetrofitClient;
import com.omnisoft.retrofitpractice.Utility.Constants;

import retrofit2.Call;

public class Notifier {
    public static void sendNotification(String recipientFCMToken, CustomAPI callBackConsumer) {
        FCMRequestBody requestBody = new FCMRequestBody(new FCMRequestData("fake title", "fake message"), recipientFCMToken);
        Call<FCMResponseBody> call = RetrofitClient.Companion.getInstance(Constants.FCM_BASE_URL).getApi().sendNotification(requestBody);
        CustomRetrofitClient.enqueue(call, "fcm", callBackConsumer);
    }
}
