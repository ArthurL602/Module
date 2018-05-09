package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 屏幕相关的工具类
 */

public class ScreenUtil {
    /**
     * 获得屏幕的宽
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
       return  context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕密度
     * @param context
     * @return
     */
    public  static float getScreenDensity(Context context){
        return  context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕密度dpi
     * @param context
     * @return
     */
    public static float getScreenDensityDpi(Context context){
        return context.getResources().getDisplayMetrics().densityDpi;

    }


    /**
     * 截屏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 截屏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }
}
