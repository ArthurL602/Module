package com.ljb.baselibrary.network.okhttp.callback;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ljb.baselibrary.network.bean.BaseResult;
import com.ljb.baselibrary.network.exception.ApiException;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 返回一个String类型的callback
 */

public abstract class  StringCallBack extends EngineCallBack<BaseResult> {
    private Gson mGson = new Gson();

    @Override
    public BaseResult parseNetworkResponse(Response response) throws Exception {
        String result = response.body().string();
        JSONObject jsonObject = new JSONObject(result);
        BaseResult baseResult = mGson.fromJson(result, BaseResult.class);
        if (baseResult.isOk()) {
            String data = jsonObject.getString("data");
            if (!TextUtils.isEmpty(data)) {
                onSuccessful(data);
            } else {
                onError(new ApiException("没有获取到相关数据"));
            }
            return baseResult;
        }
        onError(new ApiException(" code: " + baseResult.getCode() + "; " + baseResult.getMsg()));
        return null;
    }
    @Override
    public void onSuccess(BaseResult response) {

    }
    protected abstract void onSuccessful(String data);
}
