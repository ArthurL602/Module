package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

import static android.Manifest.permission.WRITE_SETTINGS;

/**
 * 屏幕相关的工具类
 */

public class ScreenUtil {
    private static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

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
     * @return
     */
    public  static float getScreenDensity(){
        return  Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕密度dpi
     * @return
     */
    public static float getScreenDensityDpi(){
        return Resources.getSystem().getDisplayMetrics().densityDpi;

    }

    /**
     * 设置屏幕为全屏
     * @param activity
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置屏幕为非全屏
     *
     */
    public static void setNonFullScreen(@NonNull final Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 切换屏幕为全屏与否状态
     * @param activity
     */
    public static void toggleFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = activity.getWindow();
        if ((window.getAttributes().flags & fullScreenFlag) == fullScreenFlag) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 判断屏幕是否为全屏
     * @param activity
     * @return
     */
    public static boolean isFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * 设置屏幕为横屏
     * @param activity
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置屏幕为竖屏
     * @param activity
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否横屏
     * @return
     */
    public static boolean isLandscape() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否竖屏
     * @return
     */
    public static boolean isPortrait() {
        return Utils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转角度
     * @param activity
     * @return
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }


    /**
     * 判断是否锁屏
     * @return
     */
    public static boolean isScreenLock() {
        KeyguardManager km =
                (KeyguardManager) Utils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
        //noinspection ConstantConditions
        return km.inKeyguardRestrictedInputMode();
    }

    /**
     * 设置进入休眠时长
     * @param duration
     */
    @RequiresPermission(WRITE_SETTINGS)
    public static void setSleepDuration(final int duration) {
        Settings.System.putInt(
                Utils.getApp().getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT,
                duration
        );
    }

    /**
     *  获取进入休眠时长
     * @return
     */
    public static int getSleepDuration() {
        try {
            return Settings.System.getInt(
                    Utils.getApp().getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return -123;
        }
    }

    /**
     * 判断是否是平板
     * @return
     */
    public static boolean isTablet() {
        return (Utils.getApp().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 适配垂直滑动的屏幕
     * @param activity
     * @param designWidthInPx
     */
    public static void adaptScreen4VerticalSlide(final Activity activity,
                                                 final int designWidthInPx) {
        adaptScreen(activity, designWidthInPx, true);
    }

    /**
     * 适配水平滑动的屏幕
     * @param activity
     * @param designHeightInPx
     */
    public static void adaptScreen4HorizontalSlide(final Activity activity,
                                                   final int designHeightInPx) {
        adaptScreen(activity, designHeightInPx, false);
    }

    /**
     * Reference from: https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
     */
    private static void adaptScreen(final Activity activity,
                                    final int sizeInPx,
                                    final boolean isVerticalSlide) {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = Utils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        if (isVerticalSlide) {
            activityDm.density = activityDm.widthPixels / (float) sizeInPx;
        } else {
            activityDm.density = activityDm.heightPixels / (float) sizeInPx;
        }
        activityDm.scaledDensity = activityDm.density * (systemDm.scaledDensity / systemDm.density);
        activityDm.densityDpi = (int) (160 * activityDm.density);

        appDm.density = activityDm.density;
        appDm.scaledDensity = activityDm.scaledDensity;
        appDm.densityDpi = activityDm.densityDpi;

        Utils.ADAPT_SCREEN_ARGS.sizeInPx = sizeInPx;
        Utils.ADAPT_SCREEN_ARGS.isVerticalSlide = isVerticalSlide;
    }

    /**
     * 适配屏幕
     * Reference from: https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
     * @param activity
     */
    public static void cancelAdaptScreen(final Activity activity) {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = Utils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity;
        activityDm.densityDpi = systemDm.densityDpi;

        appDm.density = systemDm.density;
        appDm.scaledDensity = systemDm.scaledDensity;
        appDm.densityDpi = systemDm.densityDpi;
    }

    /**
     * 取消适配屏幕
     */
    public static void cancelAdaptScreen() {
        Utils.cancelAdaptScreen();
    }

    /**
     * 恢复适配屏幕
     */
    public static void restoreAdaptScreen() {
        Utils.restoreAdaptScreen();
    }

    /**
     * 是否适配屏幕
     * @return
     */
    public static boolean isAdaptScreen() {
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        final DisplayMetrics appDm = Utils.getApp().getResources().getDisplayMetrics();
        return systemDm.density != appDm.density;
    }
}
