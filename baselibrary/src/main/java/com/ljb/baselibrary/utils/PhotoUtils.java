package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;

import com.ljb.baselibrary.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;


/**
 * Author      :ljb
 * Date        :2018/10/22
 * Description : 拍照工具类
 */
public class PhotoUtils {
    /**
     * 调用系统拍照
     *
     * @param context
     * @param outFile
     * @param requestCode
     */
    public static void takePhoto(Context context, File outFile, int requestCode) {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // android 7.0 文件管理权限
            uri = getUri(context, outFile);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = getUri(context, outFile);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(cameraIntent, requestCode);
        }
    }

    /**
     * 调用第三方剪辑
     *
     * @param srcPath 原图片路径
     * @param dst     目标图片路径
     * @param context
     * @param width
     * @param height  原图片路径不能和目标图片路径一致
     */
    public static void cropRawPhoto(String srcPath, String dst, Context context, int width, int height) {
        //这个uri是拍照后保存下来的图片文件路径
        File file = new File(srcPath);
        File dstFile = new File(dst);
        Uri uri = getUri(context, file);
        if (!dstFile.getParentFile().exists()) {
            dstFile.getParentFile().mkdirs();
        }
        // 修改配置参数（我这里只是列出了部分配置，并不是全部）
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        // 修改状态栏颜色
        options.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        // 隐藏底部工具
        options.setHideBottomControls(true);
        //是否能调整裁剪框
        options.setShowCropFrame(true);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(100);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);
        if (context instanceof Activity) {
            // 设置源uri及目标uri
            UCrop.of(uri, Uri.fromFile(dstFile))
                    // 长宽比
                    .withAspectRatio(1, 1)
                    // 图片大小
                    .withMaxResultSize(width, height)
                    // 配置参数
                    .withOptions(options).start((Activity) context);
        }
    }

    /**
     * 打开第三方相册 -- 知乎
     *
     * @param context
     * @param requestCode
     */
    public static void openRawPic(Context context, int requestCode) {
        if (context instanceof Activity) {
            Matisse.from((Activity) context).choose(MimeType.ofAll())//
                    .countable(true).maxSelectable(1)// 图片选择的最多数量
//                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                    .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new GlideEngine())//
                    .forResult(requestCode);
        }

    }

    /**
     * 剪辑图片
     *
     * @param context
     * @param inputUri
     * @param outputFile
     * @param requestCode
     */
    public static void takeCrop(Context context, Uri inputUri, File outputFile, int width, int height, int
            requestCode) {
        if (inputUri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android 7.0以上
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale ", true); //是否保留比例
        intent.putExtra("return-data", false); //是否在Intent中返回图片
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 打开相册
     *
     * @param context
     * @param requestCode
     */
    public static void openPic(Context context, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(photoPickerIntent, requestCode);
        }
    }


    /**
     * API 大于等于19的时候需要对返回的数据进行解析
     *
     * @param data
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String handleImageOfKitKat(Context context, Intent data) {
        String imageUrl = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documnets".equals(uri.getAuthority())) {//判断uri是不是media格式
                String id = docId.split(":")[1];//是media格式的话将uri进行二次解析取出id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imageUrl = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(docId));
                imageUrl = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imageUrl = getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imageUrl = uri.getPath();
        }
        return imageUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static String getImagePath(Context context, Uri externalContentUri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(externalContentUri, null, selection, null, null, null);
        if (cursor == null) {
            return path;
        }
        while (cursor.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return path;
    }

    /**
     * API小于19的时候返回的就是图片的Url
     *
     * @param data
     * @return
     */
    public static String handleImageBeforeKitKat(Intent data) {
        Uri imageUrl = data.getData();
        String imagePath = imageUrl.getPath();
        return imagePath;
    }

    /**
     * 获取uri
     *
     * @param context
     * @param srcFile
     * @return
     */
    public static Uri getUri(Context context, File srcFile) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", srcFile);
            //通过FileProvider创建一个content类型的Uri
        } else {
            uri = Uri.fromFile(srcFile);
        }
        return uri;
    }
}
