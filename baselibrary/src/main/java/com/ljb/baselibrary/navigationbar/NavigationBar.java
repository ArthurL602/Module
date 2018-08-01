package com.ljb.baselibrary.navigationbar;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Author      :ljb
 * Date        :2018/7/26
 * Description :
 */
public class NavigationBar extends AbsNavigationBar {
    private NavigationBar(Builder builder) {
        super(builder);
    }

    public static class Builder extends AbsNavigationBar.Builder<Builder>{


        public Builder(Context context, int layoutId, ViewGroup parent) {
            super(context, layoutId, parent);
        }

        @Override
        public NavigationBar create() {
            return new NavigationBar(this);
        }
    }
}
