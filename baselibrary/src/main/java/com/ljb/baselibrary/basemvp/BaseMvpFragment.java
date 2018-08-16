package com.ljb.baselibrary.basemvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljb.baselibrary.basemvp.proxy.FragmentMvpProxyImpl;


/**
 * Author      :ljb
 * Date        :2018/8/16
 * Description :所有个MVP 架构 的Fragment的基类，处理Fragment的共性内容和逻辑
 */
public abstract  class BaseMvpFragment<P extends BasePresenter> extends Fragment implements IBaseView{
    protected View mRoot;
    protected P mPresenter;
    private FragmentMvpProxyImpl mMvpProxy;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if(mRoot == null){
            mRoot =  inflater.inflate(getLayoutId(),container,false);
        }
        mPresenter =getPresenter();
        if(mPresenter!=null){
            mPresenter.attachView(this);
        }
        mMvpProxy = createMvpProxy();
        mMvpProxy.bindAndCreatePresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private FragmentMvpProxyImpl createMvpProxy() {
        if(mMvpProxy==null){
            mMvpProxy = new FragmentMvpProxyImpl(this);
        }
        return mMvpProxy;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    protected abstract int getLayoutId();
    protected abstract P getPresenter();
    protected abstract void initView();
    protected abstract void initData();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
        mMvpProxy.unbindPresenter();
    }

}
