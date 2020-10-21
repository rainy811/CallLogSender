package com.example.tongxunluf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.utils.DeviceIdUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private String[] FileExist;
    private String[] b2;
    private String[] permissionList = new String[]{    //申请的权限列表
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.MODIFY_PHONE_STATE,
    };
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
    private Timer T;
    private TimerTask TT;
    private Button B1;
    Button B2;
    // 销售名称
    private String imei = DeviceIdUtils.getDeviceId();
    private String salesName;

    // 通过函数获取手机IMEI码
    private static final String IMEI = DeviceIdUtils.getDeviceId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        ActivityCompat.requestPermissions(this, permissionList, 100);

        B2 = (Button) findViewById(R.id.B2);
        B1 = (Button) findViewById(R.id.but_id);
        final EditText et = (EditText) findViewById(R.id.editText);
        et.setEnabled(false);
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setEnabled(true);
       //         T.cancel();
            }
        });
        SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd");
        et.setText(time.format(new Date()));
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    // T.cancel();
                }
            }
        });

        salesName = SalesNameUtil.getSalesName(imei);

        addCallLOg();
        B1.setOnClickListener(new View.OnClickListener() {
                                  @RequiresApi(api = Build.VERSION_CODES.M)
                                  public void onClick(View view) {
                                      switch (view.getId()) {
                                          case R.id.but_id:
                                              //T.cancel();
                                              b = et.getText().toString();
                                              file2 = new File(local_file + "/" + b.replace("/", "-") + ".csv");
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
                                                      file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + salesName + "  通话数量：" + oo + "  通话时长" + xiaoshi + "h" + fenzhong + "m" + n2 + "s" + ".csv"));
                                                  } else if (fenzhong / 60 == 0) {
                                                      n2 = n2 - fenzhong * 60;
                                                      file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + salesName + "  通话数量：" + oo + "  通话时长" + fenzhong + "m" + n2 + "s" + ".csv"));
                                                  }
                                              } else {
                                                  file2.renameTo(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TXT" + "/" + b.replace("/", "-") + "  " + salesName + "  通话数量：" + oo + "  通话时长" + n2 + "s" + ".csv"));
                                              }
                                              number2 = 0;
                                              new Thread() {
                                                  @Override
                                                  public void run() {
                                                  }
                                              }.start();
                                              break;
                                          default:
                                              break;
                                      }
                                  }
                              }
        );
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
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String a = "0";
            if (type == 1) {
                a = "呼入";
            } else if (type == 2) {
                a = "呼出";
            }

            SimpleDateFormat time2 = new SimpleDateFormat("yyyy/MM/dd");
            String c = time2.format(new Date());
            if (!file.exists()) {
                file.mkdirs();
            }
            TelephonyManager mT = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = mT.getLine1Number();
            if (!file2.exists()) {
                file.createNewFile();
                file2.createNewFile();
            }
            b = b.replace("/", "-");
            ;
            b2 = b.split("-");
            if (b2[1].contains("0") == false) {
                b2[1] = "0" + b2[1];
            }
            if (b2[2].contains("0") == false) {
                b2[2] = "0" + b2[2];
            }
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
}