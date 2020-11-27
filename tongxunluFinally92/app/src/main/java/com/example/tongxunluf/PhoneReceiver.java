package com.example.tongxunluf;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;
    Intent requestIn;
    @Override
    public void onReceive(Context context, Intent intent) {
        requestIn = new Intent(context, ResponseService.class);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 如果是拨打电话
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            //Toast.makeText(context,"打出去了！！！",Toast.LENGTH_SHORT).show();
            //startService(context,requestIn);
        } else{
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    //Toast.makeText(context,"响铃了!!!!!!!!!!!!",Toast.LENGTH_SHORT).show();
                    startService(context,requestIn);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                    }
                    //Toast.makeText(context,"接听了！！！！！！",Toast.LENGTH_SHORT).show();
                    startService(context,requestIn);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mIncomingFlag) {
                        Log.i(TAG, "incoming IDLE");
                    }
                    //Toast.makeText(context,"挂断了！！！！！！！",Toast.LENGTH_SHORT).show();
                    startService(context,requestIn);
                    break;
            }
        }
    }

    private void startService(Context context, Intent intent){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intent);
        }else {
            context.startService(intent);
        }
    }
}
