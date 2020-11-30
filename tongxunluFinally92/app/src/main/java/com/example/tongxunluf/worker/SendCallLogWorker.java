package com.example.tongxunluf.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.tongxunluf.upload.Upload;

public class SendCallLogWorker extends Worker {
    private final String TAG = "SendCallLogWorker";
    public SendCallLogWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    /**
     * 将需要自行的方法在doWork中调用
     * @return
     */
    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG,"Worker works");
        Upload.sendBySoap();

        return Result.success();
    }

}
