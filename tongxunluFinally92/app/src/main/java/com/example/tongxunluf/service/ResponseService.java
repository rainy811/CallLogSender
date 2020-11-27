package com.example.tongxunluf.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.example.tongxunluf.upload.Upload;


public class ResponseService extends Service {
    private final String TAG = "ResponseService";
    public ResponseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Upload.sendBySoap();
        Log.i(TAG,"响应服务");
    }
}
