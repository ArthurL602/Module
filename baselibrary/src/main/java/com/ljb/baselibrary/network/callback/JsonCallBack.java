package com.ljb.baselibrary.network.callback;

import com.google.gson.Gson;
import com.ljb.baselibrary.network.Utils.NetUtils;
import com.ljb.baselibrary.network.bean.BaseResult;
import com.ljb.baselibrary.network.error.ServiceError;

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
        BaseResult baseResult = mGson.fromJson(response.body().string(), BaseResult.class);
        if (baseResult.isOk()) {
            Class<?> clz = NetUtils.analysisClassInfo(getClass(), 0);
            T t = (T) mGson.fromJson(baseResult.getData().toString(), clz);
            onSuccessful(t);
            return baseResult;
        }
        onError(new ServiceError("后台数据返回错误： code: " + baseResult.getCode()));
        return null;
    }

    @Override
    public void onSuccess(BaseResult<T> response) {

    }

    public abstract void onSuccessful(T t);
}
