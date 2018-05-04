package com.ljb.baselibrary.network.retrofit;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public class RxHttpUtils {
    static RetrofitClient mRetrofitClient = RetrofitClient.getInstance();
    private static Map<Class, Object> map = new HashMap<>();
    private static Map<String, String> moreBaseUrlMap = new HashMap<>();

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

    public static RxHttpUtils with() {
        return new RxHttpUtils();
    }

    public RxHttpUtils retrofit(Retrofit retrofit) {
        mRetrofitClient.retrofit(retrofit);
        return this;
    }

    /**
     * 添加多url
     *
     * @param key
     * @param baseUrl
     * @return
     */
    public RxHttpUtils addBaseUrl(String key, String baseUrl) {
        moreBaseUrlMap.put(key, baseUrl);
        return this;
    }

    /**
     * 根据key 获取baseUrl
     *
     * @param key
     * @return
     */
    public static String getBaseUrl(String key) {
        String baseUlr = moreBaseUrlMap.get(key);
        if (TextUtils.isEmpty(baseUlr)) {
            return "";
        }
        return baseUlr;
    }
}
