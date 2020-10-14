package com.example.googleauthfirebase.notif;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.googleauthfirebase.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private int messageId = 1000;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("PushMessage", remoteMessage.getNotification().getBody());
        String title = remoteMessage.getNotification().getTitle();
        if (title == null) {
            title = "Push Message";
        }

        String text = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.drawable.ic_action_name_notif)
                .setContentTitle(title)
                .setContentText(text);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("PushMessage", "Token" + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }

}
