package com.ljb.ui.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Author      :meloon
 * Date        :2018/3/30
 * Description :
 */

public class SimpleBehavior extends FloatingActionButton.Behavior {
    private boolean isShow;
    private LinearLayout mBottomTab;

    public SimpleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View
            directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout,
                child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int
            dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.e("TAG", "dyConsumed: "+dyConsumed);
        if (dyConsumed > 0) {//向上滑动
            if (!isShow) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                child.animate().translationY(layoutParams.bottomMargin + child.getMeasuredHeight()).setDuration(400)
                        .start();
//                mBottomTab.animate().translationY(mBottomTab.getMeasuredHeight()).setDuration(200).start();

                isShow = true;
            }

        } else if(dyConsumed<0){
            if (isShow) {
                child.animate().translationY(0).setDuration(400).start();
//                mBottomTab.animate().translationY(0).setDuration(200).start();
                isShow = !isShow;
            }
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
//        mBottomTab = parent.findViewById(R.id.bottom_tab_layout);
        return super.onLayoutChild(parent, child, layoutDirection);
    }
}
