package com.ljb.baselibrary.network.okhttp.callback;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public abstract class ResponseBodyCallback extends EngineCallBack<ResponseBody> {
    @Override
    public ResponseBody parseNetworkResponse(Response response) throws Exception {
        return response.body();
    }
}
