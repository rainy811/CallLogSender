package com.example.tongxunluf.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tongxunluf.callLog.JsonUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.mail.SendMailUtil;

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
        String content =  JsonUtils.getJson()+"";
        String title = SalesNameUtil.getSalesName();
        SendMailUtil.send("henryren@keyence.com.cn",content,title);
        Log.i("SendMailLog", "doWork()");
        return Result.success();
    }
}
