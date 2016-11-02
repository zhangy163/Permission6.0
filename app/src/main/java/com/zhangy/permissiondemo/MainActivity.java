package com.zhangy.permissiondemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/*运行时权限*/
public class MainActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST_CODE=1;

    @Bind(R.id.tv_permission_status)
    TextView typermissionStatus;

    @OnClick(R.id.btn_request_permission)
    public void onClick(){
        requestPermission();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定注解
        ButterKnife.bind(this);
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermission() {

        //首先判断当前应用有没有CAMERA权限，如果没有则进行申请
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            //第一次请求权限时，用户拒绝
            //下一次请求shouldShowRequestPersionRationale()返回true
            //向用户解释为什么需要这个权限
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                new AlertDialog.Builder(this)
                        .setMessage("申请相机权限")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请相机权限，因为里面是String[]数组，也就是说可以同时申请多个权限，不过不建议这么做
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
                            }
                        }).show();
            }else{
                //申请相机权限
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
            }
        }else{
            typermissionStatus.setText("相机权限已申请");
            typermissionStatus.setTextColor(Color.GREEN);
        }
    }

    /*此方法是权限申请的回调方法，在此方法处理权限申请成功或失败后的操作
    *
    * 因为同时可以申请多个权限，所以回调的结果是以数组方式返回的，如果用户点击允许的话，此判断为true,可以在里面处理打开摄像头的操作
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                typermissionStatus.setTextColor(Color.GREEN);
                typermissionStatus.setText("相机权限已申请");
            }else{
                //用户勾选了不在询问
                //提示用户手动打开权限
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                    Toast.makeText(this,"相机权限已经被禁止",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
