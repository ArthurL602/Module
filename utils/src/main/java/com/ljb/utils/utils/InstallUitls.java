package com.ljb.utils.utils;

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
    public static void installApkAll(Context context, File apkFile,int requestCode) {
        if (context == null || apkFile == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 8.0需要处理未知应用来源权限问题
            boolean granted = context.getPackageManager().canRequestPackageInstalls();
            if (granted) {
                installApk(context, apkFile);
            }else{
                if(context instanceof Activity){
                   // ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, requestCode);
                    startInstallPermissionSettingActivity(context);
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
    
        /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        intent.setData(packageURI);
        //设置不同版本跳转未知应用的动作
        if (Build.VERSION.SDK_INT >= 26) {
            //intent = new Intent(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
            intent.setAction(android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        } else {
            intent.setAction(android.provider.Settings.ACTION_SECURITY_SETTINGS);
        }
        ((Activity) context).startActivityForResult(intent,requestCode);
        Toast.makeText(this, "请打开未知应用安装权限", Toast.LENGTH_SHORT).show();
    }

}
