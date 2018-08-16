package com.ljb.baselibrary.base.recyclerview.refresh.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author      :ljb
 * Date        :2018/3/29
 * Description : 装饰设计模式 （添加头部和尾部的Adapter）
 */

public class WrapRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int ITEM_TYPE_HEADER = 1000000;
    private static int ITEM_TYPE_FOOTER = 2000000;

    private SparseArray<View> mFooterViews;
    private SparseArray<View> mHeaderViews;
    // 列表Adapter
    private RecyclerView.Adapter mAdapter;

    /**
     * 获取列表Adapter
     *
     * @return
     */
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public WrapRvAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mFooterViews = new SparseArray<>();
        mHeaderViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(footerView);

        }
        return mAdapter.createViewHolder(parent, viewType);
    }

    /***
     * 构建头部尾部ViewHolder
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
        //           如果有下拉刷新动画，在这里实现

            return;
        }
        if (isFooterPosition(position)) return;
        //重新计算位置
        position = position - mHeaderViews.size();
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {//判断 是否为头部
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {//判断是否为尾部
            return mFooterViews.keyAt(position - mHeaderViews.size() - mAdapter.getItemCount());
        }
        position = position - mHeaderViews.size();
        return mAdapter.getItemViewType(position);//常规列表
    }

    /**
     * 添加头部
     *
     * @param header
     */
    public void addHeaderView(View header) {
        int index = mHeaderViews.indexOfValue(header);
        if (index < 0) {
            mHeaderViews.put(ITEM_TYPE_HEADER++, header);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加尾部
     *
     * @param footer
     */
    public void addFooterView(View footer) {
        int index = mFooterViews.indexOfValue(footer);
        if (index < 0) {
            mFooterViews.put(ITEM_TYPE_FOOTER++, footer);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     *
     * @param header
     */
    public void removeHeader(View header) {
        int index = mHeaderViews.indexOfValue(header);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 移除尾部
     *
     * @param footer
     */
    public void removeFooter(View footer) {
        int index = mFooterViews.indexOfValue(footer);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }


    /**
     * 通过索引判断是不是头部
     *
     * @param position
     * @return
     */
    public boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    /**
     * 通过索引判断是不是尾部
     *
     * @param position
     * @return
     */
    public boolean isFooterPosition(int position) {
        return position >= mHeaderViews.size() + mAdapter.getItemCount();
    }

    /**
     * 判断是不是头部类型
     *
     * @param type
     * @return
     */
    public boolean isHeaderViewType(int type) {
        int pos = mHeaderViews.indexOfKey(type);
        return pos >= 0;
    }

    /**
     * 判断是否不是尾部类型
     *
     * @param type
     * @return
     */
    public boolean isFooterViewType(int type) {
        int pos = mFooterViews.indexOfKey(type);
        return pos >= 0;
    }

    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recyclerView
     */
    public void adjustSpanSize(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager glm = (GridLayoutManager) layoutManager;
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isFooterPosition(position) || isHeaderPosition(position);
                    return isHeaderOrFooter ? glm.getSpanCount() : 1;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }
}
