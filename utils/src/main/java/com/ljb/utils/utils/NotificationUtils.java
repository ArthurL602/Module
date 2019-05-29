package com.ljb.utils.utils;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author      :ljb
 * Date        :2019/2/13
 * Description : 通知相关工具类
 */
public class NotificationUtils {


    public static final int NOTIFICATION_REQUEST = 0;

    public static Notification.Builder builder(Context context,
                                               String title,
                                               String content,
                                               @DrawableRes int iconId,
                                               @DrawableRes int largeIconId,
                                               Intent intent) {
        return builder(context, title, content, iconId, largeIconId, intent, System.currentTimeMillis(), false, false, null);
    }

    /**
     * 不需要悬浮
     */
    public static Notification.Builder builder(Context context,
                                               String title,
                                               String content,
                                               @DrawableRes int iconId,
                                               @DrawableRes int largeIconId,
                                               String channelId,
                                               String channelName,
                                               Intent intent,
                                               long when,
                                               boolean taskStack,
                                               Class<?> clz) {
        return builder(context, title, content, iconId, largeIconId, channelId, channelName, intent, when, false, taskStack, clz);
    }


    /**
     * 自带默认 channel 的
     *
     * @return
     */
    public static Notification.Builder builder(Context context,
                                               String title,
                                               String content,
                                               @DrawableRes int iconId,
                                               @DrawableRes int largeIconId,
                                               Intent intent,
                                               long when,
                                               boolean fullScreen,
                                               boolean taskStack,
                                               Class<?> clz) {
        final String channelId = "NChannel_Id_" + context.getPackageName();
        final String channelName = "NChannel_Name_" + context.getPackageName();
        return builder(context, title, content, iconId, largeIconId, channelId, channelName, intent, when, fullScreen, taskStack, clz);
    }


    public static Notification.Builder builder(Context context,
                                               String title,
                                               String content,
                                               @DrawableRes int iconId,
                                               @DrawableRes int largeIconId,
                                               String channelId,
                                               String channelName,
                                               Intent intent,
                                               long when,
                                               boolean fullScreen,
                                               boolean taskStack,
                                               Class<?> clz) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        if (nm == null)
            return null;
        final Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
        }
        builder.setContentTitle(title)
                .setContentText(content)
                .setWhen(when)
                .setAutoCancel(true);
        if (iconId > 0) {
            Icon smallIcon = IconCompat.createWithResource(context, iconId).toIcon();
            builder.setSmallIcon(smallIcon);
        }
        if (largeIconId > 0) {
            Icon largeIcon = IconCompat.createWithResource(context, largeIconId).toIcon();
            builder.setLargeIcon(largeIcon);
        }
        if (intent != null) {
            PendingIntent pendingIntent = null;
            if (taskStack && clz != null) {
                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addParentStack(clz);
                taskStackBuilder.addNextIntent(intent);
                pendingIntent= taskStackBuilder.getPendingIntent(NOTIFICATION_REQUEST, PendingIntent.FLAG_UPDATE_CURRENT);
            } else {
                pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            builder.setContentIntent(pendingIntent);
            if (fullScreen) {
                Intent it = new Intent();//空意图
                PendingIntent pi = PendingIntent.getActivity(context, NOTIFICATION_REQUEST, it, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setFullScreenIntent(pi, true);
                //                builder.setDefaults(~0);
                builder.setPriority(Notification.PRIORITY_MAX);
            }
        }
        return builder;
    }

    public static void send(Context context, int id, Notification notification) {
        send(context, id, null, notification);
    }

    public static void send(Context context, int id, String tag, Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (TextUtils.isEmpty(tag)) {
            notificationManager.notify(id, notification);
        } else {
            notificationManager.notify(tag, id, notification);
        }
    }

    /**
     * 方式 一：判断是否打开了通知权限
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }
        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String
                    .class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 方式 二：判断是否打开了通知全新啊
     * @param context
     * @return
     */
    public static boolean notificationEnable(Context context) {
        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        return notification.areNotificationsEnabled();
    }

    /**
     * 两个检测悬浮窗权限的地址
     * https://github.com/zhaozepeng/FloatWindowPermission
     * https://github.com/czy1121/settingscompat
     */

    /**
     * 8.0 以上判断是否开启通知权限
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isEnableV26(Context context) {
        try {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 去设置打开通知权限
     * @param context
     */
    public static void configNotificationSetting(Context context){
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

}
