package com.ljb.baselibrary.network.okhttp.callback;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ljb.baselibrary.network.Utils.NetUtils;
import com.ljb.baselibrary.network.bean.BaseResult;
import com.ljb.baselibrary.network.exception.ApiException;

import org.json.JSONObject;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public abstract class JsonCallBack<T> extends EngineCallBack<BaseResult<T>> {
    Gson mGson = new Gson();

    @Override
    public BaseResult<T> parseNetworkResponse(Response response) throws Exception {
        String result = response.body().string();
        JSONObject jsonObject =new JSONObject(result);
        BaseResult baseResult = mGson.fromJson(result, BaseResult.class);
        if (baseResult.isOk()) {
            Class<?> clz = NetUtils.analysisClassInfo(getClass(), 0);
            String data = jsonObject.getString("data");
            if(!TextUtils.isEmpty(data)){
                T t = (T) mGson.fromJson(data, clz);
                onSuccessful(t);
            }else{
                onError(new ApiException("没有获取到相关数据"));
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
