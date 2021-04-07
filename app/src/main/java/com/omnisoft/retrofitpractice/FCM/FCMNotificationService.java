package com.omnisoft.retrofitpractice.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.omnisoft.retrofitpractice.Activities.MainActivity;
import com.omnisoft.retrofitpractice.R;

import static com.omnisoft.retrofitpractice.Utility.Constants.FCM_NOTIFICATION_CHANNEL;

public class FCMNotificationService extends FirebaseMessagingService {
    String title, message;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");
        createNotification();
    }

    private void createNotification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(FCM_NOTIFICATION_CHANNEL, "MVVM", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("MVVM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, FCM_NOTIFICATION_CHANNEL);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setAutoCancel(true)
                .setColor(Color.RED)
                .setOnlyAlertOnce(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setBigContentTitle(title)
                        .setSummaryText("Details"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_person_24)
                .setShowWhen(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(title)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentText(message);


        manager.notify(1, notificationBuilder.build());
    }
}