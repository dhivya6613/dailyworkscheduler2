package com.example.dailyworkscheduler;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MyService extends Service {

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies

        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
        super.onStartCommand(intent,flags ,startId);
        final Intent intent1 = new Intent(this, MyService.class);

        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                    // Configure the notification channel.
                    notificationChannel.setDescription("Channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.WHITE);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyService.this, "NOTIFICATION_CHANNEL_ID");

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_menu_add)
                        .setTicker("Hearty365")
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setContentTitle("work is scheduled")
                        .setContentText("Hi dhivya,do the work: ")
                        .setChannelId(NOTIFICATION_CHANNEL_ID)
                        .setLights(Color.GREEN, 3000, 3000)
                        .setOngoing(true)
                        .setContentInfo("Info");

                notificationManager.notify(/*notification id*/1, notificationBuilder.build());
                Log.i("notification", "notification is called");
                stopSelf();
            }
        },3,3,SECONDS);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in one hour
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        Log.i("bn","notify occured");
        alarm.set(
                alarm.RTC_WAKEUP,

                System.currentTimeMillis() + ( 60),
                PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
        );
    }
}
