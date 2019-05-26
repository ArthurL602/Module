package com.ljb.ui.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Author      :ljb
 * Date        :2018/4/6
 * Description : Dialog View的辅助处理类（view的一些实例化和事件监听）
 */
public class DialogViewHelper {
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;


    public DialogViewHelper() {

    }

    public DialogViewHelper(int layoutResId, Context context) {
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
        mViews = new SparseArray<>();
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
        mViews = new SparseArray<>();
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 获取实例
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public  <T extends View> T getView(int viewId) {
        WeakReference<View> weakReference = mViews.get(viewId);
        View view = null;
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param clickListener
     */
    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
