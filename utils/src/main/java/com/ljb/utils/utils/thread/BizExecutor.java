package com.ljb.utils.utils.thread;

import android.util.Log;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




/**
 * Created by LKF on 2018/9/14 0014.
 */
class BizExecutor extends ThreadPoolExecutor {
    private static final String TAG = "BizExecutor";
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int POOL_SIZE = Math.max(2, Math.min(CPU_COUNT, 8));
    private static final int KEEP_ALIVE_SECONDS = 30;

    BizExecutor(BlockingQueue<Runnable> workQueue) {
        super(POOL_SIZE, POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, workQueue, new BgThreadFactory());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            Log.e("TAG", "afterExecute(BizExecutor.java:37): afterExecute( runnable: , throwable:  ) Exception happened !!! ");
            t.printStackTrace();
        }
    }

    @Override
    public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return super.newTaskFor(runnable, value);
    }
}