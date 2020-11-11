package com.example.tongxunluf.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.tongxunluf.MainActivity;
import com.example.tongxunluf.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotifyUtil {

    @SuppressLint("WrongConstant")
    public static void notifi(String title, String text){
        Context context = ContextUtil.getInstance();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("id", "通知测试", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId("id")
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(resultPendingIntent)
                    .setFullScreenIntent(resultPendingIntent, true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setChannelId("id")
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setOngoing(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(resultPendingIntent)
                    .setFullScreenIntent(resultPendingIntent, true)
                    .setPriority(Notification.PRIORITY_MAX);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(1, notification);//把通知显示出来
        //startForeground(1,notification);//前台通知(会一直显示在通知栏)
    }
}
