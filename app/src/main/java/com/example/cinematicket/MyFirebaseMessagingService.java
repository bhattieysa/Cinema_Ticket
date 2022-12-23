package com.example.cinematicket;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification()!=null){
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification=null;
            if (SDK_INT>=O) {
                notification = new Notification.Builder(getApplicationContext(),
                        "FCM Channel").setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.mipmap.ic_appicon)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build();
            }else {
                notification=new Notification.Builder(getApplicationContext())
                        .setContentText(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSmallIcon(R.mipmap.ic_appicon)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build();
            }
            if (notification!=null)
                notificationManager.notify(122,notification);
        }else {
            Log.d("tagg", "onMessageReceived: null");
        }



    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}



// super.onMessageReceived(remoteMessage);
