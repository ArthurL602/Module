package com.ljb.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类
 */

public class ToastUtil {
    /**
     * 短时间吐司
     *
     * @param context
     * @param text
     */
    public static void showShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间吐司
     * @param context
     * @param text
     */
    public static void showLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
