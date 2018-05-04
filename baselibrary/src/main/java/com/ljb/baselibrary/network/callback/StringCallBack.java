package com.ljb.baselibrary.network.callback;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 返回一个String类型的callback
 */

public abstract class  StringCallBack extends EngineCallBack<String> {
    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return response.body().string();
    }
}
