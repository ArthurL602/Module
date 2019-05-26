package com.ljb.utils.utils;

import android.os.Build;

/**
 * Author      :ljb
 * Date        :2018/10/14
 * Description : Android系统版本工具类
 */
public class VersionUtils {
    /**
     * 是否大于Android 5.0 version_code : 21
     * @return
     */
    public static boolean isMoreThan5() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) return true;
        else return false;
    }

    /**
     * 是否大于 Android 6.0 version_code: 23
     * 0
     * @return
     */
    public static boolean isMoreThan6() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) return true;
        else return false;
    }

    /**
     * 是否大于 Android 7.0 version_code: 24
     * @return
     */
    public static boolean isMoreThan7() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return true;
        else return false;
    }

    /**
     * 是否大于 Android 8.0 version_code: 26
     * @return
     */
    public static boolean isMoreThan8() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) return true;
        else return false;
    }
}
