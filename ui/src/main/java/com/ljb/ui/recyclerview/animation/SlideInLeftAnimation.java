package com.ljb.ui.recyclerview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 从左滑入
 */

public class SlideInLeftAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{new ObjectAnimator().ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)};
    }
}
