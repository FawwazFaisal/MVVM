package com.omnisoft.retrofitpractice.FCM;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.omnisoft.retrofitpractice.App;

public class FCMTokenService extends FirebaseMessagingService {
    public FCMTokenService() {
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseFirestore.getInstance().collection("users").document(App.user.email).update("FCMToken", s).addOnSuccessListener(aVoid -> App.user.fcmToken = s);
    }
}