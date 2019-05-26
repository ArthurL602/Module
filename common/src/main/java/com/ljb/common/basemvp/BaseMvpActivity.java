package com.ljb.common.basemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ljb.common.basemvp.proxy.ActivityMvpProxyImpl;
import com.ljb.common.basemvp.proxy.IActivityMvpProxy;


/**
 * Author      :ljb
 * Date        :2018/8/14
 * Description :所有个MVP 架构 的 Activity的基类，处理Activity的共性内容和逻辑
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {
    protected P mPresenter;
    private IActivityMvpProxy mMvpProxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mMvpProxy= createMvpProxy();
        mMvpProxy.bindAndCreatePresenter();
        initView();
        initEvent();
    }

    private IActivityMvpProxy createMvpProxy() {
        if(mMvpProxy == null){
            mMvpProxy = new ActivityMvpProxyImpl<>(this);
        }
        return mMvpProxy;
    }

    protected abstract int getLayoutId();

    protected abstract P getPresenter();

    protected abstract void initView();

    protected abstract void initEvent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMvpProxy.unbindPresenter();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }

    }
}
