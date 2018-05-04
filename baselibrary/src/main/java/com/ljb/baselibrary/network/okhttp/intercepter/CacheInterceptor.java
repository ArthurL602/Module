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
 * Description :
 */

public class CacheInterceptor implements Interceptor {
    private Context mContext;
    private int max_age;
    private int max_stale;


    public CacheInterceptor(Context context, int max_age, int max_stale) {
        mContext = context;
        this.max_age = max_age;
        this.max_stale = max_stale;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetUtils.isNetWork(mContext)) {// 如果有网络|
            Request request = chain.request();
            return chain.proceed(request).newBuilder()//
                    .removeHeader("Pragma")//
                    .removeHeader("Cache-Control")//
                    .header("Cache-Control", "public,max-age=" + max_age)//单位秒
                    .build();
        } else {
            CacheControl cacheControl = new CacheControl.Builder()//
                    .maxStale(max_stale, TimeUnit.SECONDS)//
                    .build();
            Request request = chain.request().newBuilder()//
                    .cacheControl(cacheControl)//
                    .build();
            return chain.proceed(request);
        }
    }
}
