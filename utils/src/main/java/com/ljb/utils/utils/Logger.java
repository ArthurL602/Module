package com.ljb.utils.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Author      :ljb
 *  Date        :2018/5/25
 *  Description : 自定义logger类
 */

public class Logger {

    private static final String DEFAULT_TAG = "TAG";
    private static boolean isShowToLogCat = true;
    private static boolean isShowLogTime = true;
    private static boolean isShowClassName = true;
    private static boolean isShowMethonName = true;
    private static boolean isShowOutLocation = true;
    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    private static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void isShowLog(boolean isShow) {
        isShowToLogCat = isShow;
    }

    public static void trace() {
        if(isShowToLogCat) {
            Log.d(DEFAULT_TAG, getContent(getCurrentStackTraceElement()));
        }
    }

    private static String getContent(StackTraceElement trace) {
        String className = trace.getClassName().toString().substring(trace.getClassName().toString().lastIndexOf(".") + 1);
        String logTime = "[" + getSystemCurrentTime() + "]";
        String logForClassName = "[" + className + "]";
        String logForMethodName = "[" + trace.getMethodName() + "]";
        String logOutLocation = "[" + trace.getLineNumber() + "]->\n";
        return (isShowLogTime?logTime:"") + (isShowClassName?logForClassName:"") + (isShowMethonName?logForMethodName:"") + (isShowOutLocation?logOutLocation:"");
    }
    //获取系统的当前时间
    public static String getSystemCurrentTime() {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String time = e.format(curDate);
            return time;
        } catch (NullPointerException var3) {
            Logger.e(var3.toString());
        } catch (IllegalArgumentException var4) {
            Logger.e(var4.toString());
        }
        return null;
    }
    public static void traceStack() {
        if(isShowToLogCat) {
            traceStack(DEFAULT_TAG, 6);
        }
    }

    public static void traceStack(String tag, int priority) {
        if(isShowToLogCat) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            Log.println(priority, tag, stackTrace[4].toString());
            StringBuilder str = new StringBuilder();
            String prevClass = null;

            for(int i = 5; i < stackTrace.length; ++i) {
                String className = stackTrace[i].getFileName();
                int idx = className.indexOf(".java");
                if(idx >= 0) {
                    className = className.substring(0, idx);
                }

                if(prevClass == null || !prevClass.equals(className)) {
                    str.append(className.substring(0, idx));
                }

                prevClass = className;
                str.append(".").append(stackTrace[i].getMethodName()).append(":").append(stackTrace[i].getLineNumber()).append("\n");
            }

            Log.println(priority, tag, str.toString());
        }
    }

    public static void i(String tag, String message) {
        if(isShowToLogCat) {
            Log.i(tag, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void i(String message) {
        if(isShowToLogCat) {
            Log.i(DEFAULT_TAG, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void e(String tag, String message) {
        if(isShowToLogCat) {
            msg_e(tag,getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void e(String message) {
        if(isShowToLogCat) {
            msg_e(DEFAULT_TAG,getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void d(String tag, String message) {
        if(isShowToLogCat) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void d(String message) {
        if(isShowToLogCat) {
            Log.d(DEFAULT_TAG, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void v(String tag, String message) {
        if(isShowToLogCat) {
            Log.v(tag, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void v(String message) {
        if(isShowToLogCat) {
            Log.v(DEFAULT_TAG, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void w(String tag, String message) {
        if(isShowToLogCat) {
            Log.w(tag, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public static void w(String message) {
        if(isShowToLogCat) {
            Log.w(DEFAULT_TAG, getContent(getCurrentStackTraceElement()) + message);
        }
    }

    public  static void msg_e(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }
}