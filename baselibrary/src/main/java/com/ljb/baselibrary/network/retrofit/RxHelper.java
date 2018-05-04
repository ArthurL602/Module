package com.ljb.baselibrary.network.retrofit;

import com.ljb.baselibrary.network.bean.BaseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Author      :meloon
 * Date        :2018/5/4
 * Description :
 */

public class RxHelper {
    public static <T> ObservableTransformer<BaseResult<T>, BaseResult<T>> transformer() {
        return new ObservableTransformer<BaseResult<T>, BaseResult<T>>() {
            @Override
            public ObservableSource<BaseResult<T>> apply(@NonNull Observable<BaseResult<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())//
                        .unsubscribeOn(Schedulers.io())//
                        .observeOn(AndroidSchedulers.mainThread())//
                        .subscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
