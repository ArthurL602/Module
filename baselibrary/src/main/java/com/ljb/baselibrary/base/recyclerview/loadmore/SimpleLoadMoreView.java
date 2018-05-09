package com.ljb.baselibrary.base.recyclerview.loadmore;


import com.ljb.baselibrary.R;

/**
 *
 */

public class SimpleLoadMoreView extends LoadMoreView {
    @Override
    public int getLoadView() {
        return R.layout.load_more_view;
    }

    @Override
    public int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    public int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    public int getLoddEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
