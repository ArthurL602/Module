package com.ljb.baselibrary.network.okhttp;

import android.content.Context;

import com.ljb.baselibrary.network.callback.EngineCallBack;
import com.ljb.baselibrary.network.callback.UploadCallBack;

import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.RequestBody;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description : 引擎规范
 */
public interface IHttpEngine {
    // Get请求 返回一个 String类型

    <T> void get(Context context, String url, Map<String, Object> params, boolean isCache, CacheControl cacheControl,
                 EngineCallBack<T> callBack);

    // Post 表单 请求
    <T> void postForm(Context context, String url, Map<String, Object> params, EngineCallBack<T> callBack);

    // POST 其他类型的body (eg. json xml)
    <T> void postOther(Context context, String url, RequestBody requestBody, EngineCallBack<T> callBack);
    // 文件上传
    <T> void postFile(Context context, String url, Map<String, Object> params, UploadCallBack callBack);

    // https 添加证书

}
