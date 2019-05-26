package com.ljb.common.basemvp.proxy;


import com.ljb.common.basemvp.IBaseView;

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
