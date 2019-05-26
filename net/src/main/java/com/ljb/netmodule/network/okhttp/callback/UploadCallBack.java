package com.ljb.netmodule.network.okhttp.callback;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description : 上传回调接口
 */

public abstract class UploadCallBack extends EngineCallBack<String> {

    public abstract void progress(long currentLength, long total);

    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return response.body().string();
    }
}
