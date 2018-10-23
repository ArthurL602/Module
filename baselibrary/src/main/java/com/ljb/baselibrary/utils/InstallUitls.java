package com.ljb.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

/**
 * Author      :ljb
 * Date        :2018/10/23
 * Description : 安装工具类
 */
public class InstallUitls {
    /**
     * 安装APK，兼容 7.0   8.0
     * @param context
     * @param apkFile
     */
    public static void installApkAll(Context context, File apkFile) {
        if (context == null || apkFile == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0需要处理未知应用来源权限问题
            boolean granted = context.getPackageManager().canRequestPackageInstalls();
            if (granted) {
                installApk(context, apkFile);
            }else{
                if(context instanceof Activity){
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10010);
                }
            }
        } else {
            installApk(context, apkFile);
        }

    }

    /**
     *
     * 用得uCrop剪辑框架
     */
//    public void cropRawPhoto(Uri uri) {  //这个uri是拍照后保存下来的图片文件路径
//        mCropFile = new File(getExternalFilesDir("crop_img").getAbsolutePath(), System.currentTimeMillis() + ".jpg");
//        mCropFile.getParentFile().mkdirs();
//        // 修改配置参数（我这里只是列出了部分配置，并不是全部）
//        UCrop.Options options = new UCrop.Options();
//        // 修改标题栏颜色
//        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
//        //设置裁剪图片可操作的手势
//        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
//        // 修改状态栏颜色
//        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        // 隐藏底部工具
//        options.setHideBottomControls(true);
//        //是否能调整裁剪框
//        options.setShowCropFrame(true);
//        // 图片格式
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        // 设置图片压缩质量
//        options.setCompressionQuality(100);
//        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
//        // 如果不开启，用户不能拖动选框，只能缩放图片
//        options.setFreeStyleCropEnabled(true);
//
//        // 设置源uri及目标uri
//        UCrop.of(uri, Uri.fromFile(mCropFile))
//                // 长宽比
//                .withAspectRatio(1, 1)
//                // 图片大小
//                .withMaxResultSize(200, 200)
//                // 配置参数
//                .withOptions(options)
//                .start(this);
//
//    }

    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           //  apkFile：是.apk文件
//            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
