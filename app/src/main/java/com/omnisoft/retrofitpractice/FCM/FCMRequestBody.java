package com.omnisoft.retrofitpractice.FCM;

public class FCMRequestBody {
    FCMRequestData data;
    String to;

    public FCMRequestBody(FCMRequestData data, String to) {
        this.data = data;
        this.to = to;
    }

    public FCMRequestData getData() {
        return data;
    }

    public String getTo() {
        return to;
    }
}
