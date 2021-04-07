package com.omnisoft.retrofitpractice.FCM;

public class FCMRequestData {
    String title;
    String message;

    public FCMRequestData(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
    //Bitmap image;
}
