package com.ljb.netmodule.network;


import com.ljb.netmodule.network.okhttp.callback.EngineCallBack;

import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/4/10
 * Description :
 */
public abstract class HttpCallBack<T> extends EngineCallBack<T> {

        /**
     * 可以用来添加一些公用的参数
     *
     * @param tag
     * @param params
     */
    @Override
    public void onPreExecute(Object tag, Map<String, Object> params) {

        params.put("sort", "0");
        params.put("is_free", "-1");
        onPreExecute();
    }


    public abstract void onPreExecute() ;
    public abstract void onSuccess(T t);
}
