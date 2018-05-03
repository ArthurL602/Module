package com.ljb.baselibrary.http;

import android.content.Context;

import com.ljb.baselibrary.http.okhttp.OkHttpEngine;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description :
 */
public class HttpUtils {

    private String mUrl;
    private int mType = TYPE_GET;
    public static final int TYPE_POST = 0X0011;
    public static final int TYPE_GET = 0X0012;

    private Context mContext;

    private Map<String, Object> mParams;


    private static IHttpEngine sIHttpEngine = new OkHttpEngine();

    /**
     * 在Application中初始化引擎
     *
     * @param iHttpEngine
     */
    public static void init(IHttpEngine iHttpEngine) {
        sIHttpEngine = iHttpEngine;
    }

    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    /**
     * 请求的方式：POST
     *
     * @return
     */
    public HttpUtils post() {
        mType = TYPE_POST;
        return this;
    }

    /**
     * 请求的方式：GET
     *
     * @return
     */
    public HttpUtils get() {
        mType = TYPE_GET;
        return this;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    /**
     * 添加多个参数
     *
     * @param params
     * @return
     */
    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    /**
     * 添加回调
     *
     * @param callBack
     */
    public void execute(EngineCallBack callBack) {

        callBack.onPreExecute(mContext, mParams);
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        if (mType == TYPE_POST) {
            post(mUrl, mParams, callBack);
        }
        if (mType == TYPE_GET) {
            get(mUrl, mParams, callBack);
        }
    }

    /**
     * 执行回调
     */
    public void execute() {
        execute(null);
    }

    /**
     * @param iHttpEngine
     */
    public void exchangeHttpEngine(IHttpEngine iHttpEngine) {
        sIHttpEngine = iHttpEngine;
    }


    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        sIHttpEngine.get(mContext, url, params, callBack);
    }


    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        sIHttpEngine.post(mContext, url, params, callBack);
    }

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
     * 解析一个类上面的class信息
     * @param object
     * @return
     */
    public static Class<?> analysisClassInfo(Object object) {
        Type getType = object.getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) getType).getActualTypeArguments();
        return (Class<?>) types[0];
    }

}
