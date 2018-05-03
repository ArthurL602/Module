package com.ljb.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description : 引擎规范
 */
public interface IHttpEngine {
    // Get请求

    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // Post请求
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // 文件下载

    // 文件上传

    // https 添加证书

}
