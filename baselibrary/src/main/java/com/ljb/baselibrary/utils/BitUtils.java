package com.ljb.baselibrary.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

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
    /**
     * view转bitmap
     */
    public Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    /**
     * 把上面获得的bitmap传进来就可以得到圆角的bitmap了
     */
    public void bitmapInBitmap(Bitmap bitmap, ImageView imageView) {
        Bitmap tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(tempBitmap);

        //图像上画矩形
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.STROKE);//不填充
        paint.setStrokeWidth(10);  //线的宽度
        canvas.drawRect(10, 20, 100, 100, paint);
        imageView.setImageBitmap(tempBitmap);

        //画中画
        Paint photoPaint = new Paint(); // 建立画笔
        photoPaint.setDither(true); // 获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);// 过滤一些

        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());// 创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, 300, 350);// 创建一个指定的新矩形的坐标
        canvas.drawBitmap(tempBitmap, src, dst, photoPaint);// 将photo 缩放或则扩大到
        imageView.setImageBitmap(getRoundedCornerBitmap(tempBitmap));
    }

    /**
     *   生成圆角图片
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            //设置圆角大小
            final float roundPx = 30;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }



}
