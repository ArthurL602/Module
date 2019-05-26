package com.ljb.netmodule.network.retrofit.observer;

import com.google.gson.Gson;
import com.ljb.netmodule.network.Utils.NetUtils;
import com.ljb.netmodule.network.bean.BaseResult;
import com.ljb.netmodule.network.exception.ApiException;


import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public abstract class RxJsonObserver<T> implements Observer<BaseResult<T>> {
    Gson mGson = new Gson();

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull BaseResult<T> tBaseResult) {
        if (tBaseResult.isOk()) {
            Class clz = NetUtils.analysisClassInfo(this.getClass(), 0);
            T t = (T) mGson.fromJson(tBaseResult.getData().toString(), clz);
            success(t);
        } else {
            error(new ApiException(tBaseResult.getMsg()));
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        error(e);
    }

    @Override
    public void onComplete() {

    }

    public abstract void error(Throwable e);

    public abstract void success(T t);
}
