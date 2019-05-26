package com.ljb.netmodule.network.okhttp.intercepter;

import android.content.Context;


import com.ljb.netmodule.network.Utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description : addNetWorkInterceptor走的拦截器，在有网络时候才会走
 */

public class NetWorkCacheInterceptor implements Interceptor{

    private Context mContext;
    private int mMaxAge;

    public NetWorkCacheInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetUtils.isNetWork(mContext)) {// 如果有网络|
            CacheControl cacheControl = request.cacheControl();
            mMaxAge = cacheControl.maxAgeSeconds() >= 0 ? cacheControl.maxAgeSeconds() : mMaxAge;
            request = request.newBuilder()//
                    .removeHeader("Cache-Control")//
                    .build();
            return chain.proceed(request).newBuilder()//
                    .removeHeader("Pragma")//
                    .removeHeader("Cache-Control")//
                    .header("Cache-Control", "public,max-age=" + mMaxAge)//单位秒
                    .build();
        } else {
            return chain.proceed(request);
        }
    }
}
