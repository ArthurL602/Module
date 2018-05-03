package com.ljb.baselibrary.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Author      :ljb
 * Date        :2018/4/5
 * Description : View的findViewById的辅助类
 */
public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(View view) {
        mView = view;
    }

    public ViewFinder(Activity activity) {
        mActivity = activity;
    }

    /**
     * 调用findViewById 实例化View
     *
     * @param viewId View 的id
     * @return 返回View
     */
    public View findViewById(int viewId) {
        return mActivity != null ? mActivity.findViewById(viewId) : mView.findViewById(viewId);
    }
}
