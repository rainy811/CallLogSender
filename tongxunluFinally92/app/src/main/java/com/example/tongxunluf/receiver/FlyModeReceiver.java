package com.example.tongxunluf.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tongxunluf.service.ResponseService;


public class FlyModeReceiver extends BroadcastReceiver {

    private final String TAG = "FlyModeReveiver";
    private int airState = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                airState = bundle.getInt("state");
                Log.e(TAG, "飞行模式状态 1为开启状态，0为关闭状态 airState==" + airState);
                if (airState == 0) {
                    Intent requestIn = new Intent(context, ResponseService.class);
                    context.startService(requestIn);
                }
            }
        }
    }
}
