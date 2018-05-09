package com.ljb.baselibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import java.util.regex.Pattern;

/**
 * 颜色工具类
 */

public class ColorUtil {

    /**
     * 获得资源中的颜色
     *
     * @param context
     * @param resId
     * @return
     */
    public static int getResourceColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, context.getTheme());
        }
        return context.getResources().getColor(resId);
    }

    /**
     * 将十六进制的颜色代码转化为 int
     *
     * @param color
     * @return
     */
    public static int hexToColor(String color) {
        String reg = "#[a-f0-9A-F]{8}";
        if (!Pattern.matches(reg, color)) {
            color = "#00ffffff";
        }
        return Color.parseColor(color);
    }

    /**
     * 修改颜色的透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
