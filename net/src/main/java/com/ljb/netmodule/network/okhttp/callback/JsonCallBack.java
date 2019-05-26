package com.ljb.netmodule.network.okhttp.callback;

import android.text.TextUtils;


import com.alibaba.fastjson.JSON;
import com.ljb.netmodule.network.Utils.NetUtils;
import com.ljb.netmodule.network.bean.BaseResult;
import com.ljb.netmodule.network.exception.ApiException;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public abstract class JsonCallBack<T> extends EngineCallBack<BaseResult<T>> {


    @Override
    public BaseResult<T> parseNetworkResponse(Response response) throws Exception {
        String result = response.body().string();
        BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
        if (baseResult.isOk()) {
            if (baseResult.getData() != null && !TextUtils.isEmpty(baseResult.getData().toString())) {
                Class<?> clz = NetUtils.analysisClassInfo(getClass(), 0);
                T t = (T) JSON.parseObject(baseResult.getData().toString(), clz);
                onSuccessful(t);
            }else{
                onError(new ApiException("未获取到相关数据"));
            }
            return baseResult;
        }
        onError(new ApiException("后台数据返回错误： code: " + baseResult.getCode()));
        return null;
    }

    @Override
    public void onSuccess(BaseResult<T> response) {

    }

    public abstract void onSuccessful(T t);
}
