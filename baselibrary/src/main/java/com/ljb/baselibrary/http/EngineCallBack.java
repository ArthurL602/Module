package com.ljb.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/4/7
 * Description :
 */
public interface EngineCallBack{

    void onPreExecute(Context context, Map<String, Object> params);
    void onError(Exception e);

    void onSuccess(String result);


    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
