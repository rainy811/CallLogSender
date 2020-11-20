package com.example.tongxunluf;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
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

import com.example.tongxunluf.callLog.SalesNameUtil;
import com.example.tongxunluf.upload.Upload;
import com.example.tongxunluf.worker.SendCallLogWorker;

import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity {

    private EditText editText ;
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
    private static final String SEND_CALL_LOG = "SEND_CALL_LOG";

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        ActivityCompat.requestPermissions(this, permissionList, 100);


        editText = findViewById(R.id.salesmanBox);
        Button upload = findViewById(R.id.but_id);
        saveName =  findViewById(R.id.saveName);
        startWork =  findViewById(R.id.work);
        comment =  findViewById(R.id.comment);

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
                sendBySoap(view);
            }
        });
        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWork(view);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SP_WORK_HAS_STARTED,true);
                editor.apply();
                comment.setVisibility(View.INVISIBLE);
                startWork.setVisibility(View.INVISIBLE);

            }
        });

        ignoreBatteryOptimization(this);
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
        WorkManager.getInstance(MainActivity.this).enqueueUniquePeriodicWork(SEND_CALL_LOG, ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);

    }

    public void sendBySoap(View view){
        Upload.sendBySoap();
        wakeup();
    }


    //加入省电优化白名单
    public void ignoreBatteryOptimization(Activity activity) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        boolean hasIgnored = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if(!hasIgnored) {
                @SuppressLint("BatteryLife") Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:"+activity.getPackageName()));
                startActivity(intent);
            }
        }
    }
    @SuppressLint("MissingPermission")
    public void wakeup() {
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