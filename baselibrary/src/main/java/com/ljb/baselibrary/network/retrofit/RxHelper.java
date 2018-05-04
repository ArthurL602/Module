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
    public static <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T,T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())//
                        .unsubscribeOn(Schedulers.io())//
                        .observeOn(AndroidSchedulers.mainThread())//
                        .subscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
