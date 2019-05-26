package com.ljb.netmodule.network.okhttp.callback;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ljb.netmodule.network.bean.BaseResult;
import com.ljb.netmodule.network.exception.ApiException;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 返回一个String类型的callback
 */

public abstract class StringCallBack extends EngineCallBack<BaseResult> {

    @Override
    public BaseResult parseNetworkResponse(Response response) throws Exception {
        String result = response.body().string();
        BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
        if (baseResult.isOk()) {
            if (baseResult.getData() != null && !TextUtils.isEmpty(baseResult.getData().toString())) {
                    onSuccessful(baseResult.getData().toString());
            } else {
                onSuccessful(baseResult.getMsg());
            }
            return baseResult;
        }
        onError(new ApiException("code: " + baseResult.getCode() + "; " + baseResult.getMsg()));
        return null;
    }

    @Override
    public void onSuccess(BaseResult response) {

    }

    protected abstract void onSuccessful(String data);
}
