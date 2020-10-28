package com.example.tongxunluf;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.tongxunluf.callLog.JsonUtils;
import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.mail.SendMailUtil;
import com.example.tongxunluf.worker.SendCallLogWorker;

import java.util.concurrent.TimeUnit;

//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private EditText editText ;
    private Button upload;
    private Button saveName;
    private Button startWork;
    private TextView comment;

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

    //判断是否已经开启定时发送任务
    private static final String SP_WORK_STATUS = "WORK_STATUS";
    private static final String SP_WORK_HAS_STARTED = "WORK_STARTED";
    private static final String WORKNAME = "SEND_MAIL";

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        ActivityCompat.requestPermissions(this, permissionList, 100);


        editText = (EditText) findViewById(R.id.salesmanBox);
        upload = (Button) findViewById(R.id.but_id);
        saveName = (Button) findViewById(R.id.saveName);
        startWork = (Button) findViewById(R.id.work);
        comment = (TextView) findViewById(R.id.comment);

        sharedPreferences = this.getSharedPreferences(SP_WORK_STATUS,Context.MODE_PRIVATE);
        boolean workStatus = sharedPreferences.getBoolean(SP_WORK_HAS_STARTED, false);

        if(workStatus){
            startWork.setVisibility(View.INVISIBLE);
            comment.setVisibility(View.INVISIBLE);
        }

        // 如果已经输入姓名，将无法再次编辑姓名
        if(SalesNameUtil.isEmpty()){
            editText.setText(SalesNameUtil.getSalesName());
            hasName();
        }
        //保存姓名
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().length() == 0){
                    Toast.makeText(MainActivity.this, "未能正确填写", Toast.LENGTH_LONG).show();
                }
                else{
                    SalesNameUtil.saveSalesName(editText.getText().toString());
                    hasName();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendMail(view);
            }
        });
        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWork(view);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SP_WORK_HAS_STARTED,true);
                editor.commit();
                comment.setVisibility(View.INVISIBLE);
                startWork.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void hasName(){
        saveName.setVisibility(View.INVISIBLE);
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }
    public void startWork(View view){
        PeriodicWorkRequest periodicWorkRequest;
        periodicWorkRequest = new PeriodicWorkRequest.Builder(SendCallLogWorker.class,15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(MainActivity.this).enqueueUniquePeriodicWork(WORKNAME, ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);
    }

    public void sendMail(View view){
        // 通过邮件发送通话记录
        String content =  JsonUtils.getJson()+"";
//        String title = editText.getText().toString()+"的通话记录";
        String title = SalesNameUtil.getSalesName()+"的通话记录";
        SendMailUtil.send("henryren@keyence.com.cn",content,title);
    }
}