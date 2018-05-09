package com.ljb.baselibrary.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Author      :meloon
 * Date        :2018/5/9
 * Description : Bitmap处理工具类
 */

public class BitUtils {

    /**
     * 将Bitmap --> String
     *
     * @param bitmap
     * @param format
     * @param quality
     * @return
     */
    public static String bit2String(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(format, quality, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }
        return null;
    }

    /**
     * 将 String --> Bitmap
     *
     * @param str
     * @return
     */
    public static Bitmap str2Bimap(String str) {
        if (!TextUtils.isEmpty(str)) {
            byte[] b = Base64.decode(str.getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * 将 byte 数组 转换成 Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b != null && b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * 将 byte 数组 转换成 Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] b, BitmapFactory.Options options) {
        if (b != null && b.length != 0 && options == null) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else if (b != null && b.length != 0 && options != null) {
            return BitmapFactory.decodeByteArray(b, 0, b.length, options);
        }
        return null;
    }

    /**
     * 将 res --> Bitmap
     *
     * @param resources
     * @param resId
     * @return
     */
    public static Bitmap res2Bitmap(Resources resources, int resId) {
        return BitmapFactory.decodeResource(resources, resId);
    }

    /**
     * 将 res --> Bitmap
     *
     * @param resources
     * @param resId
     * @return
     */
    public static Bitmap res2Bitmap(Resources resources, int resId, BitmapFactory.Options options) {
        if (options == null) {
            return BitmapFactory.decodeResource(resources, resId);
        } else {
            return BitmapFactory.decodeResource(resources, resId, options);
        }
    }

    /**
     * 将 file --> Bitmap
     *
     * @param pathName
     * @return
     */
    public static Bitmap file2Bitmap(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 将 file --> Bitmap
     *
     * @param pathName
     * @return
     */
    public static Bitmap file2Bitmap(String pathName, BitmapFactory.Options options) {
        if (options == null) {
            return BitmapFactory.decodeFile(pathName);
        } else {
            return BitmapFactory.decodeFile(pathName, options);
        }
    }
}
