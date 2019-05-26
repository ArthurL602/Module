package com.ljb.netmodule.network.okhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description : 返回一个Bitmap类型的 callback
 */

public abstract class BitmapCallBack extends EngineCallBack<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }
}
