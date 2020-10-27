package com.example.tongxunluf.callLog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import androidx.annotation.RequiresApi;

import com.example.tongxunluf.utils.ContextUtil;
import com.example.tongxunluf.utils.DeviceIdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CallLogInfosUtils {
    private static String[] callColumn = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE
    };// 通话类型}

    //获取通话记录list
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static  List<CallLogInfo> getContentCallLogs(){
        String salesman = SalesNameUtil.getSalesName();
        List<CallLogInfo> callLogInfoList = new ArrayList<>();

        //查看获取权限
        if (ContextUtil.getInstance().checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        //系统方式获取通讯录储存地址
        Cursor cursor = ContextUtil.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                callColumn, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);// 按照时间逆序排列，最近打的最先显示
        if(cursor != null && cursor.getCount() > 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:dd");
            while (cursor.moveToNext()){
                CallLogInfo callLogInfo = new CallLogInfo();

                // 获取单条通讯录信息
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                //24小时前的时间戳
                long period = 24 * 60 * 60 * 1000;
                long lastDay = System.currentTimeMillis() -  period;
                //筛选出24小时内的通话记录，并去除未接通电话。
                if( duration > 0 && date > lastDay){
                    callLogInfo.setSalesName(salesman);
                    callLogInfo.setName(name);
                    callLogInfo.setNumber(number);
                    callLogInfo.setDuration(duration);
                    callLogInfo.setDate(simpleDateFormat.format(date));
                    callLogInfo.setTime(simpleTimeFormat.format(date));

                    if(type == 1){
                        callLogInfo.setType("呼入");
                    }else {
                        callLogInfo.setType("呼出");
                    }

                    callLogInfoList.add(callLogInfo);
                }
            }
        }
        return callLogInfoList;
    }
}
