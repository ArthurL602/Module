package com.ljb.baselibrary.navigationbar;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author      :ljb
 * Date        :2018/7/26
 * Description :
 */
public interface INavigation {
    void createNavigationBar();

    void attachNavigationParams();

    void attachParent(View navigationView, ViewGroup parent);
}
