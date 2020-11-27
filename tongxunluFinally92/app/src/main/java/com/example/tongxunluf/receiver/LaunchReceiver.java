package com.example.tongxunluf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tongxunluf.service.ResponseService;

public class LaunchReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent tIntent = new Intent(context, ResponseService.class);
        context.startService(tIntent);
    }
}
