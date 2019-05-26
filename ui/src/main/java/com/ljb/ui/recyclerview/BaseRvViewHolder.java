package com.ljb.ui.recyclerview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * RecyclerView 的ViewHolder
 */

public class BaseRvViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private BaseRvAdapter mAdapter;

    public void setAdapter(BaseRvAdapter adapter) {
        mAdapter = adapter;
    }

    public BaseRvViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseRvViewHolder setText(int viewId, String content) {
        TextView tv = getView(viewId);
        tv.setText(content);
        return this;
    }

    public BaseRvViewHolder setText(int viewId, int resId) {
        String content = itemView.getResources().getString(resId);
        setText(viewId, content);
        return this;
    }

    public BaseRvViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public BaseRvViewHolder setImageFromPath(int viewId, Object path, IImageLoader loader) {
        ImageView iv = getView(viewId);
        loader.loadImage(iv, path);
        return this;
    }

    public BaseRvViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }

    public BaseRvViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 为指定Id的控件添加点击事件
     *
     * @param viewId
     * @return
     */
    public BaseRvViewHolder addOnClickListener(int viewId) {
        View view = getView(viewId);
        if (view != null) {
            if (view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.getItemChildClickListener() != null) {
                        mAdapter.getItemChildClickListener().onItemChildClick(getLayoutPosition(), v, mAdapter);
                    }
                }
            });
        }
        return this;
    }

    /**
     *  为指定Id的控件添加长按事件
     * @param viewId
     * @return
     */
    public BaseRvViewHolder addOnLongClickListener(int viewId) {
        View view = getView(viewId);
        if (view != null) {
            if (view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mAdapter.getItemChildLongClickListener() != null && mAdapter.getItemChildLongClickListener
                            ().onItenChildLongClick(getLayoutPosition(), v, mAdapter);
                }
            });
        }
        return this;

    }

    public void setVisible(int viewId, boolean visible) {
        View  view=getView(viewId);
        view.setVisibility(visible? View.VISIBLE :View.INVISIBLE);
    }

    public interface IImageLoader {
        void loadImage(ImageView imageView, Object path);
    }
}
