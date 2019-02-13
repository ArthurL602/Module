package com.ljb.baselibrary.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljb.baselibrary.utils.InstallUitls;

import java.io.File;

/**
 * Author      :ljb
 * Date        :2018/11/26
 * Description : 安装的activity
 *
 *  适配 8.0 需要加权限：<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/>
 */
public class InstalledActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_INSTALLED = 5;
    public static final int REQUEST_CODE_UNKNOWN_APP_SOURCES = 6;
    private File mFile;

    public void installApk(File apkFile) {
        mFile = apkFile;
        InstallUitls.installApkAll(this, apkFile, REQUEST_CODE_INSTALLED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_INSTALLED:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    InstallUitls.installApkAll(InstalledActivity.this, mFile, REQUEST_CODE_INSTALLED);
                } else {
                    Uri packageURI = Uri.parse("package:"+getPackageName());
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
                    startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP_SOURCES);
                }
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && requestCode == REQUEST_CODE_UNKNOWN_APP_SOURCES) {
            InstallUitls.installApkAll(this, mFile, REQUEST_CODE_INSTALLED);
        }
    }
}
