package com.example.cinematicket;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.firebase.FirebaseApp;

public class CtApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (SDK_INT>=O) {
            NotificationChannel channel = new NotificationChannel("FCM Channel",
                    "LMS", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
