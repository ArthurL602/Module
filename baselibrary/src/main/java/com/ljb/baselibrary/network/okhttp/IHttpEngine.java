package com.ljb.baselibrary.network.okhttp;

import com.ljb.baselibrary.network.okhttp.callback.EngineCallBack;
import com.ljb.baselibrary.network.okhttp.callback.UploadCallBack;

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

    <T> void get(Object o, String url, Map<String, Object> params, boolean isCache, CacheControl cacheControl,
                 EngineCallBack<T> callBack);

    // Post 表单 请求
    <T> void postForm(Object o, String url, Map<String, Object> params, EngineCallBack<T> callBack);

    // POST 其他类型的body (eg. json xml)
    <T> void postOther(Object o, String url, RequestBody requestBody, EngineCallBack<T> callBack);
    // 文件上传
    <T> void postFile(Object o, String url, Map<String, Object> params, UploadCallBack callBack);

    // https 添加证书

}
