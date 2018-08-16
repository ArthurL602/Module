package com.ljb.baselibrary.basemvp.proxy;


import com.ljb.baselibrary.basemvp.BasePresenter;
import com.ljb.baselibrary.basemvp.IBaseView;
import com.ljb.baselibrary.basemvp.inject.InjectPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author      :ljb
 * Date        :2018/8/16
 * Description :
 */
public class MvpProxyImpl<V extends IBaseView> implements IMvpProxy {
    private V mView;
    private List<BasePresenter> mPresenters;

    public MvpProxyImpl(V view) {
        mView = view;
        mPresenters = new ArrayList<>();
    }

    @Override
    public void bindAndCreatePresenter() {
        // 对应一个Activity 对应 多个 Presenter (Dagger)
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter annotation = field.getAnnotation(InjectPresenter.class);
            if (annotation != null) {
                Class<? extends BasePresenter> presenterClazz = null;
                // 自己去判断一下类型？ 获取继承的父类，如果不是 继承 BasePresenter 抛异常
                Class<?> superclass = field.getType().getSuperclass();
                if (superclass.getCanonicalName().equals(BasePresenter.class.getCanonicalName())) {
                    presenterClazz = (Class<? extends BasePresenter>) field.getType();
                }else{
                    // 乱七八糟一些注入
                    throw new RuntimeException("No support inject presenter type " + field.getType().getName());
                }
                try {
                    BasePresenter basePresenter = presenterClazz.newInstance();
                    basePresenter.attachView(mView);
                    field.setAccessible(true);
                    field.set(mView, basePresenter);
                    mPresenters.add(basePresenter);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void unbindPresenter() {
        for (BasePresenter presenter : mPresenters) {
            presenter.detachView();
        }
        mView = null;
    }
}
