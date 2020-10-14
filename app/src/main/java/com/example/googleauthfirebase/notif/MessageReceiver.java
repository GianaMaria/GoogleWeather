package com.example.googleauthfirebase.notif;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import androidx.core.app.NotificationCompat;

import com.example.googleauthfirebase.R;

public class MessageReceiver extends BroadcastReceiver {
    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String BattPS = "No data";
        int plug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (plug == BatteryManager.BATTERY_PLUGGED_AC) {
            BattPS = "AC";
        }
        if (plug == BatteryManager.BATTERY_PLUGGED_USB) {
            BattPS = "USB";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.drawable.ic_action_name_battery)
                .setContentTitle("Broadcast Receiver")
                .setContentText(BattPS);
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++, builder.build());

    }
}
