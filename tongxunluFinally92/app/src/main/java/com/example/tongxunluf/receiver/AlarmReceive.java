package com.example.tongxunluf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.tongxunluf.service.AlarmService;

public class AlarmReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //循环启动Service
        Intent requestIn = new Intent(context, AlarmService.class);
        context.startService(requestIn);
    }
}