package com.example.administrator.b.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.administrator.b.BaseActivity;
import com.example.administrator.b.R;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    public static final int REQUEST_PERMISSION_CODE=1;
    private ImageView camera;
    private ImageView album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStatusBar();//设置沉浸式布局

        setContentView(R.layout.activity_main);

        View v= findViewById(R.id.main_layout);//设置背景透明度
        v.getBackground().setAlpha(125);//0--255为透明度值

        get_SDcard_permission(); //向用户申请相机和相册权限

        camera = (ImageView)findViewById(R.id.camera);
        album = (ImageView)findViewById(R.id.album);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);


    }


    /*设置沉浸式布局*/
    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    //获得手机相机和相册的权限
    private void get_SDcard_permission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            int checkWriteSDPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkWriteSDPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                return;
            }
        }
    }

    private void go_to_camera_process(){ //处理相机拍摄的照片
        Intent intent = new Intent(MainActivity.this,ProcessActivity.class);
        String camera_permission = "camera_permission";
        intent.putExtra("permission",camera_permission);
        startActivity(intent);
    }

    private void go_to_album_process(){  //处理相册选择的照片
        Intent intent = new Intent(MainActivity.this,ProcessActivity.class);
        String file_permission = "file_permission";
        intent.putExtra("permission",file_permission);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:
                go_to_camera_process();
                break;
            case R.id.album:
                go_to_album_process();
                break;
            default:break;
        }
    }
}
