package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Author      :ljb
 * Date        :2018/1/10
 * Description :状态栏工具类
 */

public class StatusUtils {
    private static final int COLOR_TRANSLUCENT = Color.parseColor("#00000000");

    public static final int DEFAULT_COLOR_ALPHA = 112;

    /**
     * 设置状态栏的颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api 21以上
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup parentView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBar = createStatusBar(activity, color);
            parentView.addView(statusBar);
            // 需要加FitSystemWindows,可以代码设置，也可以在xml中设置
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            View activityView = contentView.getChildAt(0);
            activityView.setFitsSystemWindows(true);
        }
    }

    /**
     * 设置DrawerLayout的状态栏
     *
     * @param activity      当前Activity
     * @param drawerLayout  DrawerLayout
     * @param isTransParent 状态栏是否透明
     * @param color         状态栏的颜色
     */
    public static void setDrawerLayoutStatus(Activity activity, DrawerLayout drawerLayout, boolean isTransParent,
                                             @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(COLOR_TRANSLUCENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewGroup contentView = (ViewGroup) drawerLayout.getChildAt(0);
        // 重置属性
        drawerLayout.setFitsSystemWindows(false);
        contentView.setFitsSystemWindows(false);
        contentView.setClipToPadding(true);
        //侧滑菜单布局
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawer.setFitsSystemWindows(false);
        if (!isTransParent) {
            //添加一个View
            contentView.addView(createStatusBar(activity, color));
        }
    }

    /**
     * 设置状态栏透明
     *
     * @param activity
     */
    public static void setStatusTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusBarColor(COLOR_TRANSLUCENT, DEFAULT_COLOR_ALPHA));
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup parentView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBar = createStatusBar(activity, COLOR_TRANSLUCENT);
            parentView.addView(statusBar);
        }
    }

    private static View createStatusBar(Activity activity, int color) {
        //实例化一个占位视图（占状态栏位置）
        View statusView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight
                (activity));
        statusView.setLayoutParams(lp);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    /**
     * 全屏覆盖(覆盖了状态栏和导航栏,手指滑动状态栏或者导航栏将弹出状态栏和导航栏)
     *
     * @param activity
     */
    public static void FullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE//
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//
                    | View.SYSTEM_UI_FLAG_FULLSCREEN//
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(option);
        }
    }

    /**
     * 设置全透明(状态栏和导航栏也透明)
     *
     * @param activity
     */
    public static void FullTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api 21以上
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View
                    .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            activity.getWindow().getDecorView().setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(COLOR_TRANSLUCENT);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup parentView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBar = createStatusBar(activity, activity.getResources().getColor(android.R.color.transparent));
            parentView.addView(statusBar);
        }
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //Get alpha color
    private static int calculateStatusBarColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


}
