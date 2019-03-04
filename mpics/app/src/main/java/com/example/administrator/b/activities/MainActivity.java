package com.example.administrator.b.activities;

import android.os.Bundle;
import android.view.View;

import com.example.administrator.b.BaseActivity;
import com.example.administrator.b.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v= findViewById(R.id.main_layout);//找到你想要设置透明背景的layout的id
        v.getBackground().setAlpha(100);//0--255为透明度值
    }
}
