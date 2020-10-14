package com.example.googleauthfirebase.notif;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.app.NotificationCompat;

import com.example.googleauthfirebase.R;

public class NetworkReceiver extends BroadcastReceiver {

    private int netId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!checkNetwork(context)){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                    .setSmallIcon(R.drawable.ic_action_name_wifi_of)
                    .setContentTitle("Broadcast Receiver")
                    .setContentText("No network connection");
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(netId++, builder.build());
        }

    }

    private boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
