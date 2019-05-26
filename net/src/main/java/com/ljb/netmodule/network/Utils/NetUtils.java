package com.ljb.netmodule.network.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :  网络请求方面的工具类
 */

public class NetUtils {
    /**
     * 拼接参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String joinParams(String url, Map<String, Object> params) {
        if (params == null || params.size() < 0) {
            return url;
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }

    /**
     * 解析一个类上面的泛型信息
     *
     * @param clz
     * @param index
     * @return
     */
    public static Class<?> analysisClassInfo(Class clz, int index) {
        Type genType = clz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetWork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static void close(Closeable closeable) throws IOException {
        if(closeable!=null){
            closeable.close();
        }
    }
    {}
}
