package com.example.tongxunluf.upload;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import com.example.tongxunluf.callLog.CallLogInfosUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.utils.ContextUtil;
import com.example.tongxunluf.utils.NotifyUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Upload {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String MYURL = "http://49.235.3.119:80/Service.asmx";
    private static final String METHOD = "SaveFile";
    private static final String SB = "SB";
    private static final String FILENAME = "fileName";
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TXT/";

    public static void upload() {
        //Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + bshift.replace("/", "-")
        // + "  " + salesmanName + "  通话数量：" + oo + "  通话时长" + durationSum + "s" + ".csv"
        String salesman = SalesNameUtil.getSalesName();
        String message = CallLogInfosUtils.getMessage();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String time =  simpleDateFormat.format(new Date());
        int callNum = CallLogInfosUtils.getTotalNum();
        String callDuration = CallLogInfosUtils.getTotalTime();
        String fileName = time + " "+salesman + "  通话数量："+ callNum + "  通话时长"+ callDuration+ ".csv";

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD);
        soapObject.addProperty(SB, message);
        soapObject.addProperty(FILENAME, fileName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(MYURL);
        httpTransportSE.debug = true;

        try {
            httpTransportSE.call(NAMESPACE + METHOD, envelope);
            wakeup();
            Object object = envelope.getResponse();
            Log.i("ConnectWebService", object.toString());
        } catch (HttpResponseException | XmlPullParserException e) {
            e.printStackTrace();
            wakeup();
            NotifyUtil.notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败，若手动发送依旧失败，请邮件联系Tech");
        } catch (IOException e) {
            e.printStackTrace();
            wakeup();
            NotifyUtil.notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败，若手动发送依旧失败，请邮件联系Tech");
        }
    }

    public static void sendBySoap(){
        new Thread(){
            @Override
            public void run(){
                upload();
            }
        }.start();
    }
    @SuppressLint("MissingPermission")
    public static void wakeup() {
        KeyguardManager km = (KeyguardManager) ContextUtil.getInstance().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager)  ContextUtil.getInstance().getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }
}
