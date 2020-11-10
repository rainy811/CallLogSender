package com.example.tongxunluf.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

//读取第一次设定的销售名称
public class SalesNameUtil {

    private static final String SP_SALESMAN_INFO = "salesman_info";
    private static final String SP_SALESMAN = "salesman";
    private static final Context context = ContextUtil.getInstance();

    public static void saveSalesName(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_SALESMAN,name);
        editor.commit();
    }

    //检查是否填写过销售姓名，填写过则不需要再次填写
    public static String getSalesName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        String salesman = sharedPreferences.getString(SP_SALESMAN, null);
        return salesman;
    }

    //检查是否填写过
    public static boolean isEmpty(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        String salesman = sharedPreferences.getString(SP_SALESMAN, null);
        return (!TextUtils.isEmpty(salesman));
    }
}
