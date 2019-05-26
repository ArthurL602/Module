package com.ljb.common.basemvp.proxy;


import com.ljb.common.basemvp.IBaseView;

/**
 * Author      :ljb
 * Date        :2018/8/16
 * Description :
 */
public class FragmentMvpProxyImpl<V extends IBaseView> extends MvpProxyImpl<V> implements IFragmentMvpProxy {

    public FragmentMvpProxyImpl(V view) {
        super(view);
    }
}
