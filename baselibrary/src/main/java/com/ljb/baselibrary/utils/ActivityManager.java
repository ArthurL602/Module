package com.ljb.baselibrary.utils;

import android.app.Activity;

import java.util.Stack;
/**
*  Author      :ljb
*  Date        :2018/7/24
*  Description :
*/
public class ActivityManager {

    private static volatile ActivityManager sActivityManager;
    private Stack<Activity> mActivities;

    public static ActivityManager getInstance() {
        if (sActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (sActivityManager == null) {
                    sActivityManager = new ActivityManager();
                }
            }
        }
        return sActivityManager;
    }

    private ActivityManager() {
        mActivities = new Stack<>();
    }

    /**
     * 绑定
     *
     * @param activity
     */
    public void attach(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 解绑 - 防止内存泄露
     *
     * @param detachActivity
     */
    public void detach(Activity detachActivity) {
        // 一般循环一边移除是错误的
//        for (Activity activity : mActivities) {
//            if (activity == detachActivity) {
//                mActivities.remove(activity);
//            }
//        }
        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mActivities.get(i);
            if(activity==detachActivity){
                mActivities.remove(i);
                i--;
                size--;
            }
        }
    }

    /**
     * 关闭当前Activity
     *
     * @param finishActivity
     */
    public void finish(Activity finishActivity) {
//        for (Activity activity : mActivities) {
//            if (activity == finishActivity) {
//                mActivities.remove(activity);
//                activity.finish();
//            }
//
//        }

        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mActivities.get(i);
            if(activity==finishActivity){
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    /**
     * 关闭当前Activity
     *
     * @param activityClass
     */
    public void finish(Class<? extends Activity> activityClass) {
//        for (Activity activity : mActivities) {
//            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
//                mActivities.remove(activity);
//                activity.finish();
//            }
//
//        }
        int size = mActivities.size();
        for (int i = 0; i < size; i++) {
            Activity activity = mActivities.get(i);
            if (activity.getClass().getCanonicalName().equals(activityClass.getCanonicalName())) {
                mActivities.remove(i);
                activity.finish();
                i--;
                size--;
            }
        }
    }

    /**
     * 关闭整个应用
     */
    public void exitApplication() {
        for (Activity activity : mActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity currentActivity() {
        return mActivities.lastElement();
    }
}
