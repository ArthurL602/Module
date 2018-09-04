package com.ljb.baselibrary.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import static android.content.ContentValues.TAG;

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
    public static Bitmap viewConversionBitmap(View v) {
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
    public static void bitmapInBitmap(Bitmap bitmap, ImageView imageView) {
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
     * 生成圆角图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            //设置圆角大小
            final float roundPx = 30;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 将Bitmap压缩到不超过某个数值
     *
     * @param srcBitmap
     * @param reqSize
     * @return
     */
    public static Bitmap compressMaxSize(Bitmap srcBitmap, int reqSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //这里100表示不压缩，把压缩后的数据存放到baos中
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 95;
        //如果压缩后的大小超出所要求的，继续压缩
        while (baos.toByteArray().length / 1024 > reqSize) {
            baos.reset();
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次减少5%质量
            if (options > 5) {//避免出现options<=0
                options -= 5;
            } else {
                break;
            }
        }
        return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
    }

    /**
     * 质量压缩
     *
     * @param srcBitmap 原Bitmap
     * @param quality   0-100
     * @return
     */
    public static Bitmap compressQuality(Bitmap srcBitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 采样率压缩
     *
     * @param resources
     * @param resId
     * @param width     目标view的宽
     * @param height    目标view的高
     * @return
     */
    public static Bitmap compressInSampleSize(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        //获取采样率
        options.inSampleSize = calculateInSampleSize_1(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 缩放法压缩
     *
     * @param srcBitmap 原Bitmap
     * @param scaleX    x方向比例
     * @param scaleY    y方向比例
     * @return
     */
    public static Bitmap compressMatrix(Bitmap srcBitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
    }

    /**
     * RGB_565压缩
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap compressRGB565(Bitmap srcBitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
    }

    /**
     * RGB_565压缩
     *
     * @param resources
     * @param resId
     * @return
     */
    public static Bitmap compressRGB565(Resources resources, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * createScaledBitmap 压缩
     *
     * @param srcBitmap
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    public static Bitmap compressScaleBitmap(Bitmap srcBitmap, int dstWidth, int dstHeight) {
        return Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
    }

    /**
     * 计算采样率 1
     *
     * @param options
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    private static int calculateInSampleSize_1(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            //计算缩放比，是2的指数
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 计算采样率 2
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize_2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }
        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return result;
    }
}
