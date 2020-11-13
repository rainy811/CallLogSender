package com.example.tongxunluf.callLog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;

import androidx.annotation.RequiresApi;

import com.example.tongxunluf.utils.ContextUtil;
import com.example.tongxunluf.utils.DeviceIdUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CallLogInfosUtils {
    private static String[] callColumn = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE
    };// 通话类型}

    /**
     * 获取通讯录信息表
     * @return
     */
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
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                //筛选出24小时内的通话记录，并去除未接通电话。
                if( isToday(date)){
                    callLogInfo.setSalesName(salesman);
                    callLogInfo.setNumber(number);
                    callLogInfo.setName(name);
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

    private static boolean isToday(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        Date old = null;
        Date now = null;
        try {
            old = simpleDateFormat.parse(simpleDateFormat.format(date));
            now = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long oldTime = old.getTime();
        long nowTime = now.getTime();

        long day = (nowTime - oldTime) / (24 * 60 * 60 * 1000);

        return day<1;
    }

    /**
     * 获取上传服务器的格式内容
     * @return
     */
    public static String getMessage(){
        List<CallLogInfo> callLogInfoList = CallLogInfosUtils.getContentCallLogs();
        StringBuffer stringBuffer = new StringBuffer();
        for (CallLogInfo callLogInfo: callLogInfoList) {
            stringBuffer.append(callLogInfo.toString());
        }
        return stringBuffer.toString();
    }

    public static int getTotalNum(){
        List<CallLogInfo> callLogInfoList = getContentCallLogs();
        return callLogInfoList.size();
    }

    public static String getTotalTime(){
        List<CallLogInfo> callLogInfoList = getContentCallLogs();
        int totalDuration = 0;
        for (CallLogInfo callLogInfo: callLogInfoList ) {
            totalDuration += callLogInfo.getDuration();
        }
        return getDateString(totalDuration);
    }

    private static String getDateString(int second){
        long millisSecond = second * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisSecond);
        Date date = calendar.getTime();
        String dateString = "";
        if (second > 60){
            SimpleDateFormat format = new SimpleDateFormat("mm分ss秒");
            dateString = format.format(date);
        }else if (second > 3600){
            SimpleDateFormat format = new SimpleDateFormat("hh时mm分ss秒");
            dateString = format.format(date);
        }
        else{
            SimpleDateFormat format = new SimpleDateFormat("ss秒");
            dateString = format.format(date);
        }
        dateString = dateString.replace("时","h");
        dateString = dateString.replace("分","m");
        dateString = dateString.replace("秒","s");
        return dateString;
    }
}
