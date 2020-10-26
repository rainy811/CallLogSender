package com.example.tongxunluf.callLog;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static String getJson(){
        List<CallLogInfo> callLogInfoList = new ArrayList<>();
        Gson gson =  new Gson();
        callLogInfoList = CallLogInfosUtils.getContentCallLogs();
        String  jsonStr =  gson.toJson(callLogInfoList);
        return jsonStr;
    }
}
