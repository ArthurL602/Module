package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * Author      :ljb
 * Date        :2018/10/22
 * Description : 拍照工具类
 */
public class PhotoUtils {
    /**
     * 调用系统拍照
     * @param context
     * @param outFile
     * @param requestCode
     */
    public static void takePhoto(Context context, File outFile,int requestCode){
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // android 7.0 文件管理权限
            uri =getUri(context,outFile);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri =getUri(context,outFile);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if(context instanceof Activity){
            ((Activity)context).startActivityForResult(cameraIntent, requestCode);
        }
    }

    /**
     *  剪辑图片
     * @param context
     * @param inputUri
     * @param outputFile
     * @param requestCode
     */
    public static void takeCrop(Context context, Uri inputUri,File outputFile,int requestCode){
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
        if(Build.MODEL.contains("HUAWEI")){
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        }else{
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale ", true) ; //是否保留比例
        intent.putExtra("return-data", false); //是否在Intent中返回图片
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat",  Bitmap.CompressFormat.JPEG.toString());
        if(context instanceof Activity){
            ((Activity)context).startActivityForResult(intent,requestCode);
        }
    }

    /**
     * 获取uri
     * @param context
     * @param srcFile
     * @return
     */
    public static Uri getUri(Context context,File srcFile){
        Uri uri=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            uri= FileProvider.getUriForFile(context, context.getPackageName() + ".provider", srcFile);//通过FileProvider创建一个content类型的Uri
        }else{
            uri = Uri.fromFile(srcFile);
        }
        return uri;
    }
}
