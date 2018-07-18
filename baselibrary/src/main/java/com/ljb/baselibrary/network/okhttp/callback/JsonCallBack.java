package com.ljb.baselibrary.network.okhttp.callback;

import com.google.gson.Gson;
import com.ljb.baselibrary.network.Utils.NetUtils;
import com.ljb.baselibrary.network.bean.BaseResult;
import com.ljb.baselibrary.network.exception.ApiException;

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
            // 这里可能json解析失败，可以通过Jsonobject 得到data的字符串
            T t = (T) mGson.fromJson(baseResult.getData().toString(), clz);
            onSuccessful(t);
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
