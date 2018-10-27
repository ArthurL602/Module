package com.ljb.baselibrary.network.okhttp.callback;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ljb.baselibrary.network.Utils.NetUtils;
import com.ljb.baselibrary.network.bean.BaseResult;
import com.ljb.baselibrary.network.exception.ApiException;

import java.util.List;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 返回集合
 */

public abstract class JsonListCallBack<T> extends EngineCallBack<BaseResult<T>> {

    @Override
    public BaseResult<T> parseNetworkResponse(Response response) throws Exception {
        String result = response.body().string();
        BaseResult baseResult = JSON.parseObject(result, BaseResult.class);
        if (baseResult.isOk()) {
            if (baseResult.getData() != null && !TextUtils.isEmpty(baseResult.getData().toString())) {
                final Class<?> clz = NetUtils.analysisClassInfo(getClass(), 0);
                List<T> list = (List<T>) JSON.parseArray(baseResult.getData().toString(), clz);
                if (list != null && list.size() > 0) {
                    onSuccessful(list);
                }else{
                    onError(new ApiException("没有获取到相关数据"));
                }
            } else {
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

    public abstract void onSuccessful(List<T> list);
}
