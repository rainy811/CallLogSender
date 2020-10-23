package com.example.tongxunluf.callLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.tongxunluf.utils.ContextUtil;

public class SalesNameUtil {

    private static final String SP_SALESMAN_INFO = "salesman_info";
    private static final String SP_SALESMAN = "salesman";

    //检查是否填写过销售姓名，填写过则不需要再次填写
    public static String getSalesName() {
        // TODO: 2020/10/21 使用SharedPreferences，添加文本框，在第一次使用软件时保存销售名称。
        Context context = ContextUtil.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        String salesman = sharedPreferences.getString(SP_SALESMAN, null);
        if(!TextUtils.isEmpty(salesman)){
            return salesman;
        }
        else {
            salesman = "未能正确填写";
        }
        return salesman;
    }

    //检查是否填写过
    public static boolean isEmpty(){
        Context context = ContextUtil.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        String salesman = sharedPreferences.getString(SP_SALESMAN, null);
        return (!TextUtils.isEmpty(salesman));
    }
}
