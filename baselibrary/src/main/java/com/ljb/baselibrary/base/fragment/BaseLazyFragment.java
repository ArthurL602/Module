package com.ljb.baselibrary.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment实现懒加载的基类
 */

public abstract class BaseLazyFragment extends Fragment {

    /**
     * 是否为第一次加载数据
     */
    private boolean isFirst = true;
    /**
     * Fragment是否可见
     */
    private boolean isFragmentVisible;

    /**
     * 根布局
     */
    private View rootView;

    /**
     * 获取布局Id
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        //解决当TabLayout跳转到一个没有预加载过的Fragment，连续调用两次setUserVisibleHint方法，
        // 但此时rootView为空，不能进行加载。需要在onCreateView()
        // 方法最后判断是否进行网络加载
        if (isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true);
            isFirst = false;

        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//当用户可见
            //Fragment也可见
            isFragmentVisible = true;
        }
        //规避TabLayout跳转到一个没有预加载过的Fragment，连续调用两次setUserVisibleHint方法，
        // 但此时rootView为空，不能进行加载;
        if (rootView == null) {
            return;
        }
        //如果是第一次加载数并且Fragment是可见的，去加载数据
        if (isFirst && isFragmentVisible) {
            onFragmentVisibleChange(true);
            isFirst = false;//已经加载过一次数据了，设置成false,确保数据只加载一次
            return;
        }
        //由可见---->不可见
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    /**
     * 重置数据
     */
    protected void resetData() {
        isFirst = true;
        isFragmentVisible = false;
        rootView = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//resetData();
    }

    protected abstract void onFragmentVisibleChange(boolean isVisible);
//    private void onFragmentVisibleChange(boolean visible) {
//        if (visible) {//如果是可见状态
//
//        } else {//由可见状态到不可见状态
//
//        }
//    }
}
