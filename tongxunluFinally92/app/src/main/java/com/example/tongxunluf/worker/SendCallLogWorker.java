package com.example.tongxunluf.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tongxunluf.callLog.JsonUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.mail.SendMailUtil;
import com.example.tongxunluf.utils.ContextUtil;

import java.util.Calendar;

public class SendCallLogWorker extends Worker {
    private final static String TAG = "SendCallLogWorker";

    private static final String FLAG = "MAILWOKKER";
    private static final String SP_IS_SENDED = "IS_SENDED";
    private static boolean isSended = false;

    private SharedPreferences sharedPreferences;

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
        sharedPreferences = ContextUtil.getInstance().getSharedPreferences(FLAG,Context.MODE_PRIVATE);
        isSended = sharedPreferences.getBoolean(SP_IS_SENDED,false);
//        send();
        // 每天17点检查是否发送
        if (compareCurrentHour(17)){
            if(!isSended){
                //在指定时间，且没有发送过邮件,
                sharedPreferences.edit().putBoolean(SP_IS_SENDED,true);
                //写入已发送
                sharedPreferences.edit().commit();
                //并立刻发送
                send();
            }
            // 在指定时间，已经推送过了，则不在推送
            else{
                return Result.retry();
            }
        }else{
            //不在时间段，则重置标志位false
            sharedPreferences.edit().putBoolean(SP_IS_SENDED,false);
            sharedPreferences.edit().commit();
            return Result.retry();
        }
        return Result.success();
    }

    private void send(){
        String content =  JsonUtils.getJson()+"";
        String title = SalesNameUtil.getSalesName();
        SendMailUtil.send("henryren@keyence.com.cn",content,title);
        Log.i(TAG, "doWork()");
    }
    private boolean compareCurrentMinute(int minute){
        int current = Calendar.getInstance().get(Calendar.MINUTE);
        return current==minute;
    }
    //比较时间
    private boolean compareCurrentHour(int hour){
        int current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return current == hour;
    }
}
