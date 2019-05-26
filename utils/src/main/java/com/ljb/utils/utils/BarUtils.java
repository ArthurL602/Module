package com.ljb.utils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

import static android.Manifest.permission.EXPAND_STATUS_BAR;

/**
 * Author      :ljb
 * Date        :2018/1/10
 * Description :状态栏/导航栏/通知栏工具类
 */

public class BarUtils {
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
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight
                ());
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
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
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
///////////////////////////////////////////////////////////////////////////
    // 状态栏
    ///////////////////////////////////////////////////////////////////////////

    private static final int    DEFAULT_ALPHA = 112;
    private static final String TAG_COLOR     = "TAG_COLOR";
    private static final String TAG_ALPHA     = "TAG_ALPHA";
    private static final String TAG_OFFSET    = "TAG_OFFSET";
    private static final int    KEY_OFFSET    = -123;

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 设置状态栏是否可见
     * @param activity
     * @param isVisible
     */
    public static void setStatusBarVisibility(@NonNull final Activity activity,
                                              final boolean isVisible) {
        setStatusBarVisibility(activity.getWindow(), isVisible);
    }

    /**
     * 设置状态栏是否可见
     * @param activity
     * @param isVisible
     */
    public static void setStatusBarVisibility(@NonNull final Window window,
                                              final boolean isVisible) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            showColorView(window);
            showAlphaView(window);
            addMarginTopEqualStatusBarHeight(window);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hideColorView(window);
            hideAlphaView(window);
            subtractMarginTopEqualStatusBarHeight(window);
        }
    }

    /**
     * 判断状态栏是否可见
     * @param activity
     * @return
     */
    public static boolean isStatusBarVisible(@NonNull final Activity activity) {
        int flags = activity.getWindow().getAttributes().flags;
        return (flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
    }

    /**
     * 设置状态栏是否为浅色模式
     * @param activity
     * @param isLightMode
     */
    public static void setStatusBarLightMode(@NonNull final Activity activity,
                                             final boolean isLightMode) {
        setStatusBarLightMode(activity.getWindow(), isLightMode);
    }

    /**
     * 设置状态栏是否为浅色模式
     * @param window
     * @param isLightMode
     */
    public static void setStatusBarLightMode(@NonNull final Window window,
                                             final boolean isLightMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (isLightMode) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

    /**
     * 为 view 增加 MarginTop 为状态栏高度
     * @param view
     */
    public static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        view.setTag(TAG_OFFSET);
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, true);
    }

    /**
     * 为 view 减少 MarginTop 为状态栏高度
     * @param view
     */
    public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Object haveSetOffset = view.getTag(KEY_OFFSET);
        if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin - getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(KEY_OFFSET, false);
    }
    /**
     * 为 window 增加 MarginTop 为状态栏高度
     * @param window
     */
    private static void addMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        addMarginTopEqualStatusBarHeight(withTag);
    }
    /**
     * 为 window 减少 MarginTop 为状态栏高度
     * @param window
     */
    private static void subtractMarginTopEqualStatusBarHeight(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        View withTag = window.getDecorView().findViewWithTag(TAG_OFFSET);
        if (withTag == null) return;
        subtractMarginTopEqualStatusBarHeight(withTag);
    }

    /**
     * 设置状态栏颜色
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color) {
        setStatusBarColor(activity, color, DEFAULT_ALPHA, false);
    }

    /**
     * 设置状态栏透明度
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param alpha    The status bar's alpha which isn't the same as alpha in the color.
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarColor(activity, color, alpha, false);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param alpha    The status bar's alpha which isn't the same as alpha in the color.
     * @param isDecor  True to add fake status bar in DecorView,
     *                 false to add fake status bar in ContentView.
     */
    public static void setStatusBarColor(@NonNull final Activity activity,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        hideAlphaView(activity);
        transparentStatusBar(activity);
        addStatusBarColor(activity, color, alpha, isDecor);
    }

    /**
     * 设置状态栏颜色
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar,
                                         @ColorInt final int color) {
        setStatusBarColor(fakeStatusBar, color, DEFAULT_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param alpha         The status bar's alpha which isn't the same as alpha in the color.
     */
    public static void setStatusBarColor(@NonNull final View fakeStatusBar,
                                         @ColorInt final int color,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        fakeStatusBar.setVisibility(View.VISIBLE);
        transparentStatusBar((Activity) fakeStatusBar.getContext());
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight();
        fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
    }

    /**
     * 设置状态栏透明度
     *
     * @param activity The activity.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity) {
        setStatusBarAlpha(activity, DEFAULT_ALPHA, false);
    }

    /**
     * 设置状态栏透明度
     *
     * @param activity The activity.
     * @param alpha    The status bar's alpha.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        setStatusBarAlpha(activity, alpha, false);
    }

    /**
     * 设置状态栏透明度
     *
     * @param activity The activity.
     * @param alpha    The status bar's alpha.
     * @param isDecor  True to add fake status bar in DecorView,
     *                 false to add fake status bar in ContentView.
     */
    public static void setStatusBarAlpha(@NonNull final Activity activity,
                                         @IntRange(from = 0, to = 255) final int alpha,
                                         final boolean isDecor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        hideColorView(activity);
        transparentStatusBar(activity);
        addStatusBarAlpha(activity, alpha, isDecor);
    }

    /**
     * 设置状态栏透明度
     *
     * @param fakeStatusBar The fake status bar view.
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar) {
        setStatusBarAlpha(fakeStatusBar, DEFAULT_ALPHA);
    }

    /**
     * 设置状态栏透明度
     *
     * @param fakeStatusBar The fake status bar view.
     * @param alpha         The status bar's alpha.
     */
    public static void setStatusBarAlpha(@NonNull final View fakeStatusBar,
                                         @IntRange(from = 0, to = 255) final int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        fakeStatusBar.setVisibility(View.VISIBLE);
        transparentStatusBar((Activity) fakeStatusBar.getContext());
        ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = getStatusBarHeight();
        fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
    }

    /**
     * 为 DrawerLayout 设置状态栏颜色
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                final boolean isTop) {
        setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, DEFAULT_ALPHA, isTop);
    }

    /**
     * 为 DrawerLayout 设置状态栏颜色
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param alpha         The status bar's alpha which isn't the same as alpha in the color.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarColor4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @ColorInt final int color,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        drawer.setFitsSystemWindows(false);
        transparentStatusBar(activity);
        setStatusBarColor(fakeStatusBar, color, isTop ? alpha : 0);
        for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
            drawer.getChildAt(i).setFitsSystemWindows(false);
        }
        if (isTop) {
            hideAlphaView(activity);
        } else {
            addStatusBarAlpha(activity, alpha, false);
        }
    }

    /**
     * 为 DrawerLayout 设置状态栏透明度
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        drawerLayout
     * @param fakeStatusBar The fake status bar view.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                final boolean isTop) {
        setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, DEFAULT_ALPHA, isTop);
    }

    /**
     * 为 DrawerLayout 设置状态栏透明度
     * <p>DrawLayout must add {@code android:fitsSystemWindows="true"}</p>
     *
     * @param activity      The activity.
     * @param drawer        drawerLayout
     * @param fakeStatusBar The fake status bar view.
     * @param alpha         The status bar's alpha.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity,
                                                @NonNull final DrawerLayout drawer,
                                                @NonNull final View fakeStatusBar,
                                                @IntRange(from = 0, to = 255) final int alpha,
                                                final boolean isTop) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        drawer.setFitsSystemWindows(false);
        transparentStatusBar(activity);
        setStatusBarAlpha(fakeStatusBar, isTop ? alpha : 0);
        for (int i = 0, len = drawer.getChildCount(); i < len; i++) {
            drawer.getChildAt(i).setFitsSystemWindows(false);
        }
        if (isTop) {
            hideAlphaView(activity);
        } else {
            addStatusBarAlpha(activity, alpha, false);
        }
    }

    private static void addStatusBarColor(final Activity activity,
                                          final int color,
                                          final int alpha,
                                          boolean isDecor) {
        ViewGroup parent = isDecor ?
                (ViewGroup) activity.getWindow().getDecorView() :
                (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        } else {
            parent.addView(createColorStatusBarView(activity, color, alpha));
        }
    }

    private static void addStatusBarAlpha(final Activity activity,
                                          final int alpha,
                                          boolean isDecor) {
        ViewGroup parent = isDecor ?
                (ViewGroup) activity.getWindow().getDecorView() :
                (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        } else {
            parent.addView(createAlphaStatusBarView(activity, alpha));
        }
    }

    private static void hideColorView(final Activity activity) {
        hideColorView(activity.getWindow());
    }

    private static void hideAlphaView(final Activity activity) {
        hideAlphaView(activity.getWindow());
    }

    private static void hideColorView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void hideAlphaView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    private static void showColorView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static void showAlphaView(final Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.VISIBLE);
    }

    private static int getStatusBarColor(final int color, final int alpha) {
        if (alpha == 0) return color;
        float a = 1 - alpha / 255f;
        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return Color.argb(255, red, green, blue);
    }

    private static View createColorStatusBarView(final Context context,
                                                 final int color,
                                                 final int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
        statusBarView.setTag(TAG_COLOR);
        return statusBarView;
    }

    private static View createAlphaStatusBarView(final Context context, final int alpha) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setTag(TAG_ALPHA);
        return statusBarView;
    }

    private static void transparentStatusBar(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // action bar
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取 ActionBar 高度
     *
     * @return the action bar's height
     */
    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (Utils.getApp().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(
                    tv.data, Utils.getApp().getResources().getDisplayMetrics()
            );
        }
        return 0;
    }

/********************************************   通知栏     ***************************************************/

    /**
     * 设置通知栏是否可见
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />}</p>
     *
     * @param isVisible True to set notification bar visible, false otherwise.
     */
    @SuppressLint("SupportAnnotationUsage")
    @RequiresPermission(EXPAND_STATUS_BAR)
    public static void setNotificationBarVisibility(final boolean isVisible) {
        String methodName;
        if (isVisible) {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "expand" : "expandNotificationsPanel";
        } else {
            methodName = (Build.VERSION.SDK_INT <= 16) ? "collapse" : "collapsePanels";
        }
        invokePanels(methodName);
    }

    private static void invokePanels(final String methodName) {
        try {
            @SuppressLint("WrongConstant")
            Object service = Utils.getApp().getSystemService("statusbar");
            @SuppressLint("PrivateApi")
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName);
            expand.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 导航栏
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取导航栏高度
     *
     */
    public static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 设置导航栏是否可见
     *
     * @param isVisible 是否可见
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setNavBarVisibility(@NonNull final Activity activity, boolean isVisible) {
        setNavBarVisibility(activity.getWindow(), isVisible);
    }

    /**
     * 设置导航栏是否可见
     *
     * @param isVisible 是否可见
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    public static void setNavBarVisibility(@NonNull final Window window, boolean isVisible) {
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        final View decorView = window.getDecorView();
        if (isVisible) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~uiOptions);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOptions);
        }
    }

    /**
     * 判断导航栏是否可见
     *
     */
    public static boolean isNavBarVisible(@NonNull final Activity activity) {
        return isNavBarVisible(activity.getWindow());
    }

    /**
     * 判断导航栏是否可见
     *
     */
    public static boolean isNavBarVisible(@NonNull final Window window) {
        View decorView = window.getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        return (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
    }

    /**
     * 设置导航栏颜色
     * @param activity
     * @param color
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Activity activity, @ColorInt final int color) {
        setNavBarColor(activity.getWindow(), color);
    }

    /**
     * 设置导航栏颜色
     * @param window
     * @param color
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setNavBarColor(@NonNull final Window window, @ColorInt final int color) {
        window.setNavigationBarColor(color);
    }

    /**
     *  获取导航栏颜色
     * @param activity
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Activity activity) {
        return getNavBarColor(activity.getWindow());
    }

    /**
     *  获取导航栏颜色
     * @param window
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getNavBarColor(@NonNull final Window window) {
        return window.getNavigationBarColor();
    }

    /**
     * 判断是否支持导航栏
     * @return
     */
    public static boolean isSupportNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
            //noinspection ConstantConditions
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y || realSize.x != size.x;
        }
        boolean menu = ViewConfiguration.get(Utils.getApp()).hasPermanentMenuKey();
        boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        return !menu && !back;
    }

}
