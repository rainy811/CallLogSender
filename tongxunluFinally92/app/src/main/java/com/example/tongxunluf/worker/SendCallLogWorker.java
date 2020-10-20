package com.example.tongxunluf.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SendCallLogWorker extends Worker {

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

        Log.i("SendMailLog", "doWork()");
        return Result.success();
    }
}
