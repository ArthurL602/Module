package com.ljb.baselibrary.base.recyclerview.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.ljb.baselibrary.base.recyclerview.refresh.adapter.WrapRvAdapter;


/**
 * Author      :ljb
 * Date        :2018/3/29
 * Description : 适配头部和尾部的RecylerView
 */

public class WrapRecyclerView extends RecyclerView {
    //包裹了一层头部底部的adapter
    private WrapRvAdapter mWrapRvAdapter;
    //这个是列表Adapter
    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }
        mAdapter = adapter;
        if (mAdapter instanceof WrapRvAdapter) {
            mWrapRvAdapter = (WrapRvAdapter) mAdapter;
        } else {
            mWrapRvAdapter = new WrapRvAdapter(mAdapter);
        }
        super.setAdapter(mWrapRvAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRvAdapter.adjustSpanSize(this);
    }

    /**
     * 添加头部
     *
     * @param header
     */
    public void addHeaderView(View header) {
        if (mWrapRvAdapter == null) return;
        mWrapRvAdapter.addHeaderView(header);
    }

    /**
     * 添加尾部
     *
     * @param footer
     */
    public void addFooterView(View footer) {
        if (mWrapRvAdapter == null) return;
        mWrapRvAdapter.addFooterView(footer);
    }

    /**
     * 移除头部
     *
     * @param header
     */
    public void removeHeaderView(View header) {
        if (mWrapRvAdapter == null) return;
        mWrapRvAdapter.removeHeader(header);
    }

    /**
     * 移除尾部
     *
     * @param footer
     */
    public void removeFooterView(View footer) {
        if (mWrapRvAdapter == null) return;
        mWrapRvAdapter.removeFooter(footer);
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyItemRemoved(positionStart);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyItemChanged(positionStart);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyItemChanged(positionStart, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRvAdapter != mAdapter) mWrapRvAdapter.notifyItemInserted(positionStart);
        }
    };
}
