package com.ljb.fix.crash;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/5/16
 * Description : 统一捕捉异常，单例
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionCrashHandler mInstance;
    // 用来获取应用的信息
    private Context mContext;
    // 系统默认的异常处理类
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        // 设置全局异常类为本类
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("TAG", "uncaughtException   报异常了" + e.getLocalizedMessage());
        // 写入本地文件（ex  当前版本  ）
        // 1. 崩溃详细信息
        String errorMsg = obtainExceptionInfo(e);
        // 2. 应用信息 版本号 包名    // 3. 设备信息
        String deviceInfo = getDeviceInfo();
        // 4. 保存文件
        StringBuffer sb = new StringBuffer();
        sb.append(errorMsg).append("\n").append(deviceInfo);
        String fileName = saveToFile(sb.toString());

        cacheCrashFile(fileName);
        // 上传的问题 (保存文件，应用再次启动才上传)

        // 系统默认处理异常,如：弹窗 ,退出应用，打印log
        mDefaultExceptionHandler.uncaughtException(t, e);
    }

    /**
     * 缓存 错误信息文件
     *
     * @param fileName
     */
    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit();
    }

    /**
     * 获取崩溃文件名称
     *
     * @return
     */
    public File getCrashFile() {
        String crashFileName = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE).getString
                ("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }

    /**
     * 将信息保存到文件
     */
    private String saveToFile(String msg) {
        String fileName = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);
            // 先删除文件
            if (dir.exists()) {
                deleteDir(dir);
            }
            // 再创建文件夹
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fileName = dir.toString() + File.separator + getAssignTime() + ".txt";
                fos = new FileOutputStream(fileName);
                fos.write(msg.getBytes());
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return fileName;
    }

    private String getAssignTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 递归删除文件
     *
     * @param dir
     */
    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * 获取系统未捕获的错误信息
     *
     * @param e
     * @return
     */
    private String obtainExceptionInfo(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public String getDeviceInfo() {
        StringBuffer sb = new StringBuffer();
        HashMap<String, String> map = obtainSimpleInfo(mContext);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取手机的一些简单信息
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            map.put("versionName", packageInfo.versionName);
            map.put("versionCode", "" + packageInfo.versionCode);
            map.put("MODEL", "" + Build.MODEL);
            map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
            map.put("PRODUCT", "" + Build.PRODUCT);
            map.put("MOBLE_INFO", getMobileInfo());
        }
        return map;
    }

    /**
     * 获取手机的信息
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getFields();
            for (Field field : fields) {
                // 设置私有可用
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
