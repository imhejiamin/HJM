package com.example.administrator.b.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.b.BaseActivity;
import com.example.administrator.b.R;

public class WelcomeActivity extends BaseActivity {

    private TextView jumpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setStatusBar();//设置沉浸式布局

        setContentView(R.layout.activity_welcome);

        jumpTextView = (TextView)findViewById(R.id.jump);
        mCountDownTimer.start();
        jumpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
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

    //计时器
    private CountDownTimer mCountDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long l) {
            jumpTextView.setText("跳过 " + (l / 1000 + 1));
        }

        @Override
        public void onFinish() {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };
}
