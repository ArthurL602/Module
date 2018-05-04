package com.ljb.baselibrary.network.Utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public class AndroidPlatform {
    public static AndroidPlatform get() {
        return new AndroidPlatform();
    }

    public Executor defaultCallbackExecutor() {
        return new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Executor {

        private final Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}
