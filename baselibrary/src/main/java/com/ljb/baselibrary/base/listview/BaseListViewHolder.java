package com.ljb.baselibrary.base.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * listview holder的基类
 */

public class BaseListViewHolder {
    private SparseArray<View> mViws;
    private View mConvertView;
    private Context mContext;

    public View getConvertView() {
        return mConvertView;
    }

    private BaseListViewHolder(int layoutId, ViewGroup parent, Context context) {
        mViws = new SparseArray<>();
        mContext = context;
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static BaseListViewHolder getHolder(int layoutId, View convertView, ViewGroup parent, Context context) {
        BaseListViewHolder holder = null;
        if (convertView == null) {
            holder = new BaseListViewHolder(layoutId, parent, context);
        } else {
            holder = (BaseListViewHolder) convertView.getTag();
        }

        return holder;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViws.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViws.put(viewId, view);
        }
        return (T) view;
    }

    public BaseListViewHolder setText(int viewId, String content) {
        TextView tv = getView(viewId);
        tv.setText(content);
        return this;
    }
    public BaseListViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }

    public BaseListViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public BaseListViewHolder setText(int viewId, int resId) {
        String content = mContext.getResources().getString(resId);
        setText(viewId, content);
        return this;
    }

    public BaseListViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public BaseListViewHolder setImageFromPath(int viewId, Object path, IImageLoader loader) {
        ImageView iv = getView(viewId);
        loader.loadImage(iv, path);
        return this;
    }

    public interface IImageLoader {
        void loadImage(ImageView imageView, Object path);
    }
}
