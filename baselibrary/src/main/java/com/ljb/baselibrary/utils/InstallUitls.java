package com.ljb.baselibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

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
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10010);
                }
            }
        } else {
            installApk(context, apkFile);
        }

    }

    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           //  apkFile：是.apk文件
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
