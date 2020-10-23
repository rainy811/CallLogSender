package com.example.tongxunluf.callLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.tongxunluf.utils.ContextUtil;

public class SalesNameUtil {

    private static final String SP_SALESMAN_INFO = "salesman_info";
    private static final String SP_SALESMAN = "salesman";

    public static void saveSalesName(String name){
        Context context = ContextUtil.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_SALESMAN,name);
        editor.commit();
    }

    //检查是否填写过销售姓名，填写过则不需要再次填写
    public static String getSalesName() {
        Context context = ContextUtil.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_SALESMAN_INFO, Context.MODE_PRIVATE);
        String salesman = sharedPreferences.getString(SP_SALESMAN, null);
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
