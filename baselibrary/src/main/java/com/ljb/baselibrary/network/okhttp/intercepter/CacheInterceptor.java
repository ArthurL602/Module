package com.ljb.baselibrary.network.okhttp.intercepter;

import android.content.Context;

import com.ljb.baselibrary.network.Utils.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : addInterceptor中的拦截器，有网无网都会走
 */

public class CacheInterceptor implements Interceptor {
    private Context mContext;
    public CacheInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetUtils.isNetWork(mContext)) {// 如果有网络|
            return chain.proceed(chain.request());
        } else {
            int maxStale = chain.request().cacheControl().maxStaleSeconds();
            CacheControl cacheControl = new CacheControl.Builder()//
                    .maxStale(maxStale, TimeUnit.SECONDS)//
                    .build();
            Request request = chain.request().newBuilder()//
                    .cacheControl(cacheControl)//
                    .build();
            return chain.proceed(request);
        }
    }
}
