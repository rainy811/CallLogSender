package com.example.tongxunluf.callLog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.example.tongxunluf.utils.ContextUtil;
import com.example.tongxunluf.utils.DeviceIdUtils;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class CallLogInfos {


    private List<CallLogInfo> callLogInfos;

    public List<CallLogInfo> getCallLogInfos() {
        return callLogInfos;
    }

    public void setCallLogInfos(List<CallLogInfo> callLogInfos) {
        this.callLogInfos = callLogInfos;
    }

    private static String[] callColumn = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE
    };// 通话类型}

    //获取通话记录list
    public static List<CallLogInfo>  getContentCallLogs(){
        String imei = DeviceIdUtils.getDeviceId();
        String salesname = SalesNameUtil.getSalesName(imei);
        List<CallLogInfo> callLogInfoList = new ArrayList<>();
        if (ContextUtil.getInstance().checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor cursor = ContextUtil.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                callColumn
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        while (cursor.moveToNext()) {
            CallLogInfo callLogInfo = new CallLogInfo();
            callLogInfo.setSalesName(salesname);
            callLogInfo.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            callLogInfo.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            callLogInfo.setDuration(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            callLogInfo.setDate(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            callLogInfo.setType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));

            callLogInfoList.add(callLogInfo);
        }
        return callLogInfoList;
    }
}
