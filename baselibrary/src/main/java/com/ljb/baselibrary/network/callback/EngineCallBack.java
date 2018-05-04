package com.ljb.baselibrary.network.callback;

import android.content.Context;

import java.util.Map;

import okhttp3.Response;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description :
 */
public abstract class EngineCallBack<T> {
    public void onPreExecute(Context context, Map<String, Object> params) {

    }
    public abstract void onError(Throwable e);

    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onSuccess(T response);

    public final static EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {


        @Override
        public void onError(Throwable e) {

        }

        @Override
        public Object parseNetworkResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onSuccess(Object response) {

        }
    };
}
