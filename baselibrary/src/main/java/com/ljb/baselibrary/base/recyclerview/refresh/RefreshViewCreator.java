package com.ljb.baselibrary.base.recyclerview.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author      :ljb
 * Date        :2018/3/29
 * Description : 创建下拉刷新的辅助类
 */

public interface RefreshViewCreator {
    /**
     * 获取下拉刷新View
     *
     * @param context
     * @param parent
     * @return
     */
    View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     *
     * @param currentDragHeightm
     * @param refreshViewHeight
     * @param currentRefreshStatusL
     */
    void onPull(int currentDragHeightm, int refreshViewHeight, int currentRefreshStatusL);

    /**
     * 正在刷新中
     */
    void onRefreshing();

    /**
     * 停止刷新
     */
    void onStopRefresh();
}
