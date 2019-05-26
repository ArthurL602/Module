package com.ljb.netmodule.network.Utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import io.reactivex.annotations.NonNull;

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
