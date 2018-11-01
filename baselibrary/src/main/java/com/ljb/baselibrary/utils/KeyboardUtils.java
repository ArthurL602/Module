package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 *  Author      :ljb
 *  Date        :2018/11/1
 *  Description : 软键盘工具类
 */

public final class KeyboardUtils {
    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 显示软键盘
     * @param activity
     */
    public static void showSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 显示软键盘用 toggle
     * @param view
     */
    public static void showSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        //noinspection ConstantConditions
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

}