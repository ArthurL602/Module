package com.ljb.netmodule.network.okhttp;


import com.ljb.netmodule.network.okhttp.callback.EngineCallBack;
import com.ljb.netmodule.network.okhttp.callback.UploadCallBack;

import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

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
    public static final int TYPE_POST_OTHER = 0X0013;
    public static final int TYPE_POST_FILE = 0X0014;

    private Object mTag;

    private Map<String, Object> mParams;
    private boolean isCache = false;
    private CacheControl mCacheControl;
    private RequestBody mRequestBody;

    private static IHttpEngine sIHttpEngine = new OkHttpEngine();

    /**
     * 在Application中初始化引擎
     *
     * @param iHttpEngine
     */
    public static void init(IHttpEngine iHttpEngine) {
        sIHttpEngine = iHttpEngine;
    }


    private HttpUtils(Object tag) {
        mTag = tag;
        mParams = new HashMap<>();
    }

    public HttpUtils() {
        mParams = new HashMap<>();
        mTag = null;
    }

    public static HttpUtils with() {
        return new HttpUtils();
    }

    public static HttpUtils with(Object o) {
        if (o == null) return new HttpUtils();
        return new HttpUtils(o);
    }

    /**
     * 请求的方式：POST
     *
     * @return
     */
    public HttpUtils postForm() {
        mType = TYPE_POST;
        return this;
    }

    /**
     * 上传文件
     *
     * @return
     */
    public HttpUtils postFile() {
        mType = TYPE_POST_FILE;
        return this;
    }

    /**
     * 提交其他类型的body
     *
     * @return
     */
    public HttpUtils postOtherType() {
        mType = TYPE_POST_OTHER;
        return this;
    }

    /**
     * 设置自定义的okhttpclient
     *
     * @param okHttpClient
     * @return
     */
    public HttpUtils client(OkHttpClient okHttpClient) {
        if (sIHttpEngine instanceof OkHttpEngine) {
            ((OkHttpEngine) sIHttpEngine).setClient(okHttpClient);
        }
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

    public HttpUtils requestBody(RequestBody requestBody) {
        mRequestBody = requestBody;
        return this;
    }

    /**
     * 是否需要缓存
     *
     * @param isCache
     * @return
     */
    public HttpUtils isCache(boolean isCache) {
        this.isCache = isCache;
        return this;
    }

    /**
     * 缓存策略
     *
     * @param cacheControl
     * @return
     */
    public HttpUtils cacheControl(CacheControl cacheControl) {
        this.mCacheControl = cacheControl;
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
    public <T> void execute(EngineCallBack<T> callBack) {
        callBack.onPreExecute(mTag, mParams);
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }

        if (mType == TYPE_POST) {
            postForm(mUrl, mParams, callBack);
        }
        if (mType == TYPE_GET) {
            get(mUrl, mParams, callBack);
        }
        if (mType == TYPE_POST_OTHER) {
            if (mRequestBody == null) {
                throw new NullPointerException("RequestBody 不能为空");
            }
            postOther(mUrl, mRequestBody, callBack);
        }
        if (mType == TYPE_POST_FILE) {
            if (callBack instanceof UploadCallBack) {
                postFile(mUrl, mParams, (UploadCallBack) callBack);
            } else {
                throw new IllegalArgumentException("请传入一个UploadCallBack");
            }
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

    /**
     * get请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        sIHttpEngine.get(mTag, url, params, isCache, mCacheControl, callBack);
    }

    /**
     * 上传表单数据
     *
     * @param url
     * @param params
     * @param callBack
     */
    private void postForm(String url, Map<String, Object> params, EngineCallBack callBack) {
        sIHttpEngine.postForm(mTag, url, params, callBack);
    }

    /**
     * 上传其他类型的数据 json xml等
     *
     * @param url
     * @param requestBody
     * @param callBack
     * @param <T>
     */
    private <T> void postOther(String url, RequestBody requestBody, EngineCallBack<T> callBack) {
        sIHttpEngine.postOther(mTag, url, requestBody, callBack);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param callBack
     * @param <T>
     */
    private <T> void postFile(String url, Map<String, Object> params, UploadCallBack callBack) {
        sIHttpEngine.postFile(mTag, url, params, callBack);
    }
}
