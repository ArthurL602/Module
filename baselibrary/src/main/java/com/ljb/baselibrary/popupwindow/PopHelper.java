package com.ljb.baselibrary.popupwindow;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Author      :ljb
 * Date        :2018/5/10
 * Description : 对View的处理类
 */

public class PopHelper {
    private Context mContext;
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;

    public View getContentView() {
        return mContentView;
    }

    public PopHelper(int layoutViewId, Context context) {
        mContentView = LayoutInflater.from(context).inflate(layoutViewId, null);
        mContext = context;
        mViews = new SparseArray<>();
    }

    public PopHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public void setText(int viewId, CharSequence charSequence) {
        TextView tv = getView(viewId);
        tv.setText(charSequence);
    }

    public <V extends View> V getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<View>(view));
        }
        return (V) view;
    }

    public void setOnclickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }
}
