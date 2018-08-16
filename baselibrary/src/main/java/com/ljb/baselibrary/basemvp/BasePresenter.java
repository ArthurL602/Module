package com.ljb.baselibrary.basemvp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author      :ljb
 * Date        :2018/8/14
 * Description : 所有Presenter的基类
 */
public class BasePresenter<V extends IBaseView> {
    private V mView;

    protected V mProxyView;

    public void attachView(V view) {
        mView = view;
        // 用代理对象
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(),
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 执行这个方法 ，调用的是被代理的对象
                if (mView == null) return null;
                return method.invoke(mView, args);
            }
        });
    }

    public void detachView() {
        mView = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }
}
