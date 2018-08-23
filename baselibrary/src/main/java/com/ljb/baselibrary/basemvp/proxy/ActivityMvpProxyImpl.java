package com.ljb.baselibrary.basemvp.proxy;


import com.ljb.baselibrary.basemvp.IBaseView;

/**
 * Author      :ljb
 * Date        :2018/8/16
 * Description :
 */
public class ActivityMvpProxyImpl<V extends IBaseView> extends MvpProxyImpl<V> implements IActivityMvpProxy{
    public ActivityMvpProxyImpl(V view) {
        super(view);
    }
}
