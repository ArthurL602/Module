package com.ljb.baselibrary.network.retrofit;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public class RxHttp {
    static RetrofitClient mRetrofitClient = RetrofitClient.getInstance();
    private static Map<Class, Object> map = new HashMap<>();

    public <T> T create(Class<T> clz) {
        return createClass(clz);
    }

    private <T> T createClass(Class<T> clz) {
        T t = (T) map.get(clz);
        if (t == null) {
            t = mRetrofitClient.create(clz);
            map.put(clz, t);
        }
        return t;
    }

    public static RxHttp with() {
        return new RxHttp();
    }

    public RxHttp retrofit(Retrofit retrofit) {
        mRetrofitClient.retrofit(retrofit);
        return this;
    }
}
