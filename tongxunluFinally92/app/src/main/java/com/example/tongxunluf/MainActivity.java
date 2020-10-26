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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.tongxunluf.callLog.JsonUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.mail.SendMailUtil;
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

    private EditText editText ;
    private Button upload;
    private Button saveName;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        ActivityCompat.requestPermissions(this, permissionList, 100);

        editText = (EditText) findViewById(R.id.salesmanBox);
        upload = (Button) findViewById(R.id.but_id);
        saveName = (Button) findViewById(R.id.saveName);

        //保存姓名
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().length() == 0){
                    Toast.makeText(MainActivity.this, "未能正确填写", Toast.LENGTH_LONG).show();
                }
                else{
                    SalesNameUtil.saveSalesName(editText.getText().toString());
                    saveName.setVisibility(View.INVISIBLE);
                    editText.setEnabled(false);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendMail(view);
            }
        });
    }

    public void sendMail(View view){
        // 通过邮件发送通话记录
        String content =  JsonUtils.getJson()+"";
        String title = editText.getText().toString()+"的通话记录";
        SendMailUtil.send("henryren@keyence.com.cn",content,title);
    }
}