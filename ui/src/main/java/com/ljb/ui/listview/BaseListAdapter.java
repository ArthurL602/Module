package com.ljb.ui.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * listview 适配器adapter的基类
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected List<T> mDatas;
    protected Context mContext;
    protected int mLayoutId;

    public List<T> getDatas() {
        return mDatas;
    }

    public BaseListAdapter(List<T> datas, Context context, int layoutId) {
        mDatas = datas;
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas != null ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseListViewHolder holder = BaseListViewHolder.getHolder(mLayoutId, convertView, parent, mContext);
        T t = mDatas.get(position);
        convert(holder, t, position);
        return holder.getConvertView();
    }

    public abstract void convert(BaseListViewHolder holder, T data, int position);

    /**
     * 移除所有数据
     */
    public void removeAllDatas() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加更多数据
     *
     * @param data
     */
    public void addMoreData(@NonNull List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 删除某个位置的数据
     *
     * @param positon
     */
    public void removeData(int positon) {
        mDatas.remove(positon);
        notifyDataSetChanged();
    }

    /**
     * 在某个位置添加数据
     *
     * @param position
     * @param data
     */
    public void addDataInPosition(int position, @NonNull List<T> data) {
        mDatas.addAll(position, data);
        notifyDataSetChanged();

    }

    /**
     * 设置新数据，即删除现有的数据，添加新的数据
     *
     * @param datas
     */
    public void setNewData(@NonNull List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
}
