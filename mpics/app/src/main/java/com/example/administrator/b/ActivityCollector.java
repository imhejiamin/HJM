package com.example.administrator.b;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 * 说明：该类主要用于管理整个app应用中的所有activity，提供将当前的活动添加入列，移除当前的活动，以及移除所有的活动并推出整个程序。
 */

public class ActivityCollector {

    //活动管理的队列
    public static List<Activity> activities = new ArrayList<>();
    /**
     * 将队列添加入活动管理器
     *
     * @param activity 活动对象
     * @return void
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除当前队列中的某个活动
     *
     * @param activity 活动对象
     * @return void
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /***
     * 移除整个活动管理器中的所有活动
     * @return void
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }

        }
    }

    public static void finishBesideCurrentActivity(Activity remainActivity){

        String remainActivityName   = remainActivity.getClass().getSimpleName();
        for (Activity activity:activities){
            String activityName = activity.getClass().getSimpleName();
            if(!activityName.equals(remainActivityName)&&!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
