package com.ljb.ui.navigationbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author      :ljb
 * Date        :2018/7/26
 * Description :
 */
public class AbsNavigationBar implements INavigation {
    private Builder mBuilder;
    private View mNavigationView;

    protected AbsNavigationBar(Builder builder) {
        mBuilder = builder;
        createNavigationBar();
    }

    @Override
    public void createNavigationBar() {
        mNavigationView = LayoutInflater.from(mBuilder.mContext)//
                .inflate(mBuilder.mLayoutId, mBuilder.mParent, false);
        // 添加
        attachParent(mNavigationView, mBuilder.mParent);
        // 绑定参数
        attachNavigationParams();
    }

    /**
     * 绑定参数
     */
    @Override
    public void attachNavigationParams() {
        Map<Integer, CharSequence> textMaps = mBuilder.mTextMaps;
        for (Map.Entry<Integer, CharSequence> entry : textMaps.entrySet()) {
            TextView textView = findViewById(entry.getKey());
            textView.setText(entry.getValue());
        }
        Map<Integer, View.OnClickListener> onClickListenerMap = mBuilder.mOnClickListenerMaps;
        for (Map.Entry<Integer, View.OnClickListener> entry : onClickListenerMap.entrySet()) {
            View view = findViewById(entry.getKey());
            view.setOnClickListener(entry.getValue());
        }

    }

    public  <T extends View> T findViewById(int viewId) {
        return mNavigationView.findViewById(viewId);
    }

    /**
     * t
     * 添加到父View中
     *
     * @param navigationView
     * @param parent
     */
    @Override
    public void attachParent(View navigationView, ViewGroup parent) {
        parent.addView(navigationView, 0);
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public abstract static class Builder<T extends Builder> {
        public Context mContext;
        public int mLayoutId;
        public ViewGroup mParent;
        public Map<Integer, CharSequence> mTextMaps;
        public Map<Integer, View.OnClickListener> mOnClickListenerMaps;

        public Builder(Context context, int layoutId, ViewGroup parent) {
            mContext = context;
            mLayoutId = layoutId;
            mParent = parent;
            mTextMaps = new HashMap<>();
            mOnClickListenerMaps = new HashMap<>();
        }

        public abstract AbsNavigationBar create();

        public T setText(int viewId, String msg) {
            mTextMaps.put(viewId, msg);
            return (T) this;
        }

        public T setOnclickListener(int viewId, View.OnClickListener listener) {
            mOnClickListenerMaps.put(viewId, listener);
            return (T) this;
        }
    }
}
