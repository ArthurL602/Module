package com.ljb.framelibrary.callback;

import android.content.Context;

import com.google.gson.Gson;
import com.ljb.baselibrary.network.callback.EngineCallBack;
import com.ljb.baselibrary.network.okhttp.HttpUtils;

import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/4/10
 * Description :
 */
public abstract class HttpCallBack<T> implements EngineCallBack {
    /**
     * 可以用来添加一些公用的参数
     *
     * @param context
     * @param params
     */
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {

        params.put("sort", "0");
        params.put("is_free", "-1");
        onPreExecute();
    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        T t = (T) gson.fromJson(result, HttpUtils.analysisClassInfo(this));
        onSuccess(t);
    }

    public abstract void onPreExecute() ;
    public abstract void onSuccess(T t);
}
