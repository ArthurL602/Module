package com.ljb.ui.recyclerview.loadmore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import com.ljb.ui.recyclerview.BaseRvViewHolder;


/**
 *
 */

public abstract class LoadMoreView {
    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_END = 4;

    private int mLoadMoreStatus = STATUS_DEFAULT;

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void convert(BaseRvViewHolder holder) {
        switch (mLoadMoreStatus) {
            case STATUS_LOADING:
                visibleLoading(holder, true);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_FAIL:
                visibleLoading(holder, false);
                visibleLoadFail(holder, true);
                visibleLoadEnd(holder, false);
                break;
            case STATUS_END:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, true);
                break;
            case STATUS_DEFAULT:
                visibleLoading(holder, false);
                visibleLoadFail(holder, false);
                visibleLoadEnd(holder, false);
                break;
        }
    }



    private void visibleLoadEnd(BaseRvViewHolder holder, boolean visible) {
        holder.setVisible(getLoddEndViewId(), visible);
    }


    private void visibleLoadFail(BaseRvViewHolder holder, boolean visible) {
        holder.setVisible(getLoadFailViewId(), visible);
    }


    private void visibleLoading(BaseRvViewHolder holder, boolean visible) {
        holder.setVisible(getLoadingViewId(), visible);
    }

    public abstract
    @LayoutRes
    int getLoadView();

    public abstract
    @IdRes
    int getLoadingViewId();

    public
    @IdRes
    abstract int getLoadFailViewId();

    public
    @IdRes
    abstract int getLoddEndViewId();
}
