package com.i3developer.bastar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG ="MY_TAG";

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String,String> mapData = remoteMessage.getData();
        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("title",mapData.get("title"));
        hashMap.put("message",mapData.get("message"));
        Log.d(TAG,"TITLE "+mapData.get("title"));
        Log.d(TAG,"MSG "+mapData.get("message"));

        displayNotification(mapData.get("title"), mapData.get("message"));
    }



    public void displayNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setVibrate(new long[]{0,500,100,500,1000,500,1000,500,1000});

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0,500,100,500,1000,500,1000,500,1000});
            channel.enableLights(true);
            mNotifyMgr.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        if (mNotifyMgr != null) {
            mNotifyMgr.notify((int) (Math.random()*100), builder.build());
        }
    }
}
