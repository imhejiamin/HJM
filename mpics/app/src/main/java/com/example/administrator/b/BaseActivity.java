package com.example.administrator.b;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.b.utils.LogUtil;


/**
 * 说明：该类为所有activity 的基类，覆盖了父类的oncreate（）方法和onDestory()的方法，
 * 在oncreate（）方法中将当前活动添加进活动管理器中，在ondestory（）方法中将活动移除
 */

public class BaseActivity extends AppCompatActivity {

    private String activityName = getClass().getSimpleName();

    /***
     * 覆盖父类的oncreate方法，将当前活动添加进活动管理器中，并显示当前的活动名称
     * @param savedInstanceState  保存当前的数据
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        /*
        设置整个app 的显示布局，这里设置只能是竖屏显示，避免你一些不必要的麻烦
         */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        LogUtil.w(activityName, "- - - - - - - -> " + "onCreate()");
        ActivityCollector.addActivity(this);//将活动添加进活动管理器
    }

    /**
     * 活动的start周期
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(activityName, "- - - - - - - -> " + "onStart()");
    }

    /**
     * 用户见到的界面周期，可以与用户交互的一个活动期
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(activityName, "- - - - - - - -> " + "onResume()");
    }

    /**
     * 暂停期，活动可见但是不能交互的时候，比如说活动前面有一个弹窗，遮挡了原来的界面，所以影响了
     * 原来的界面的交互
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(activityName, "- - - - - - - -> " + "onPause()");
    }

    /**
     * 活动处于不可见的状态，停止与用户交互，可以在这里释放掉对应的资源
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.w(activityName, "- - - - - - - -> " + "onStop()");
    }

    /***
     * 将当前活动销毁，并将当前活动从活动管理器中移除
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(activityName, "- - - - - - - -> " + "onDestroy()");
        ActivityCollector.removeActivity(this);
    }

    /**
     * 活动被重新启动
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(activityName, "- - - - - - - -> " + "onRestart()");
    }
}

