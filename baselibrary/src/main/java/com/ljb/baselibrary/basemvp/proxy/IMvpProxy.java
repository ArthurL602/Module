package com.ljb.baselibrary.basemvp.proxy;

/**
 * Author      :ljb
 * Date        :2018/8/16
 * Description :
 */
public interface IMvpProxy {
    void bindAndCreatePresenter();// 创建和绑定

    void unbindPresenter();// 解绑
}
