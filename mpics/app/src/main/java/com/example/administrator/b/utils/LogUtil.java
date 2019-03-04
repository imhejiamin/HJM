package com.example.administrator.b.utils;

import android.util.Log;

/**
 * 说明：一款简单的日志管理工具，可以自己手动设置显示信息，对于调试版本和发布版本比较容易控制
 * 发布的时候只要将level设置成noting等级就可以屏蔽日志了
 * 函数分别有Log.v(String tag, String message)
 * 函数分别有Log.d(String tag, String message)
 * 函数分别有Log.i(String tag, String message)
 * 函数分别有Log.w(String tag, String message)
 * 函数分别有Log.e(String tag, String message)
 */


public class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARNNING = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;
    private static int level = VERBOSE;
//    private static int level = NOTHING;

    /***
     * verbose日志管理函数
     * @param tag 标签，一般用来标识哪一个activity活动的日志
     * @param message 日志信息，所有零碎的日志信息，必须为字符串
     */
    public static void v(String tag, String message) {
        if (level <= VERBOSE) {
            Log.v(tag, message);
        }
    }

    /***
     * debug日志管理函数
     * @param tag 标签，一般用来标识哪一个activity活动的日志
     * @param message 日志信息，关于debug或以上等级的日志信息，必须为字符串
     */
    public static void d(String tag, String message) {
        if (level <= DEBUG) {
            Log.d(tag, message);
        }
    }

    /***
     * debug 日志管理
     * @param tag 标签，一般用来标识哪一个activity活动的日志或者自己定义的一个标记，方便自己查找到对应的问题所在
     * @param message 日志信息，关于debug或以上等级的日志信息，必须为字符串
     * @param throwable 抛出异常
     */
    public static void d(String tag, String message, Throwable throwable) {
        if (level <= DEBUG) {
            Log.d(tag, message, throwable);
        }
    }

    /***
     * info日志管理函数
     * @param tag 标签，一般用来标识哪一个activity活动的日志
     * @param message 日志信息，关于info或以上等级的日志信息，必须为字符串
     */
    public static void i(String tag, String message) {
        if (level <= INFO) {
            Log.i(tag, message);
        }
    }

    /***
     * info 日志管理函数
     * @param tag 标签，一般用来标记哪一个活动的activity的活动日志
     * @param message 日志信息，一般关于info或者以上的日志信息，必须为字符串
     * @param throwable 异常抛出的提示
     */
    public static void i(String tag, String message, Throwable throwable) {
        if (level <= INFO) {
            Log.i(tag, message, throwable);
        }
    }


    /***
     * warning 日志管理函数
     * @param tag 标签，一般用来标识哪一个activity活动的日志
     * @param message 日志信息，关于warning或以上等级的日志信息，必须为字符串
     */
    public static void w(String tag, String message) {
        if (level <= WARNNING) {
            Log.w(tag, message);
        }
    }

    /****
     * warnning 日志管理函数
     * @param tag 标签，一般用于标识是哪一个activity的活动的日志
     * @param message 日志信息，关于warning或者以上的等级的日志信息，必须我字符串
     * @param throwable 异常信息
     */
    public static void w(String tag, String message, Throwable throwable) {
        if (level <= WARNNING) {
            Log.w(tag, message, throwable);
        }
    }

    /***
     * error 日志管理函数
     * @param tag 标签，一般用来标识哪一个activity活动的日志
     * @param message 日志信息，关于error或以上等级的日志信息，必须为字符串
     */

    public static void e(String tag, String message) {
        if (level <= ERROR) {
            Log.e(tag, message);
        }
    }

    /****
     * error 日志管理工具
     * @param tag 标签，一般用于标识哪一个activity活动的日志
     * @param message 日志信息，关于error或者以上等级的日志信息，必须为字符串
     * @param throwable 抛出异常的信息
     */
    public static void e(String tag, String message, Throwable throwable) {
        if (level <= ERROR) {
            Log.e(tag, message, throwable);
        }
    }
}
