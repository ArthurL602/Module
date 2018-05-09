package com.ljb.baselibrary.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Author      :ljb
 * Date        :2018/1/11
 * Description : Resouce工具类
 */

public class ResUtils {
    /**
     * 获取图片数组
     * @param context
     * @param resId
     * @return
     */
    public static int[] getDrawableArray(Context context, int resId) {
        TypedArray ta = context.getResources().obtainTypedArray(resId);
        int[] ids = new int[ta.length()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = ta.getResourceId(i, 0);
        }
        return ids;
    }

    public static String[] getStringArray(Context context,int resId) {
        return context.getResources().getStringArray(resId);
    }

    public static String getString(Context context,int resId) {
        return context.getResources().getString(resId);
    }
    public static int getInteger(Context context,int resId) {
        return context.getResources().getInteger(resId);
    }
}
