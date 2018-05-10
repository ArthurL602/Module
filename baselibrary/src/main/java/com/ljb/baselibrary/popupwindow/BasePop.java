package com.ljb.baselibrary.popupwindow;

import android.content.Context;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

/**
 * Author      :ljb
 * Date        :2018/5/10
 * Description : Pop外部调用类，不含实现代码，仅仅代理实现
 */

public class BasePop {
    private PopController mPopController;


    public BasePop() {
        mPopController = new PopController();
    }

    /**
     * 显示popupwindow
     */
    public void show() {
        mPopController.show();
    }

    /**
     * 隐藏popupwindow
     */
    public void dismiss() {
        mPopController.dismiss();
    }

    /**
     * 获取controller类
     * @return
     */
    public PopController getPopController() {
        return mPopController;
    }

    /**
     * 设置监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mPopController.setOnClickListener(viewId, listener);
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mPopController.setText(viewId, text);
    }

    /**
     * 获取实例
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return mPopController.getView(viewId);
    }

    /**
     *
     */
    public static class Builder {

        private PopController.Params mParams;

        public Builder(Context context, Window window) {
            mParams = new PopController.Params();
            mParams.mContext = context;
            mParams.window=window;
        }

        public Builder setContentView(int layoutResViewId) {
            mParams.layoutResViewId = layoutResViewId;
            return this;
        }

        public Builder setContentView(View view) {
            mParams.view = view;
            return this;
        }

        public Builder addAnimation(@StyleRes int styleAnimation) {
            mParams.styleAnimation = styleAnimation;
            return this;
        }

        public Builder setWidthAndHeight(int width, int height) {
            mParams.width = width;
            mParams.height = height;
            return this;
        }


        public Builder setCancelable(boolean cancel) {
            mParams.cancelAble = cancel;
            return this;
        }

        public Builder showAsDropDown(View anchor) {
            mParams.isDropDown = true;
            mParams.anchor = anchor;
            return this;
        }

        public Builder showAsDropDown(View anchor, int xoff, int yoff) {
            mParams.isDropDown = true;
            mParams.anchor = anchor;
            mParams.xoff = xoff;
            mParams.yoff = yoff;
            return this;
        }

        public Builder setText(int viewId, CharSequence msg) {
            mParams.mTextArray.put(viewId, msg);
            return this;

        }

        public Builder setOnclickListener(int viewId, View.OnClickListener onClickListener) {
            mParams.mClickArray.put(viewId, onClickListener);
            return this;
        }

        public BasePop create() {
            BasePop basePop = new BasePop();
            mParams.apply(basePop.getPopController());
            return basePop;
        }

        public BasePop show() {
            BasePop basePop = create();
            basePop.getPopController().show();
            return basePop;
        }

        public Builder showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
            mParams.isDropDown = true;
            mParams.anchor = anchor;
            mParams.xoff = xoff;
            mParams.yoff = yoff;
            mParams.gravity = gravity;
            return this;
        }

        public Builder showAtLocation(View parent, int gravity, int xoff, int yoff) {
            mParams.isDropDown = false;
            mParams.parent = parent;
            mParams.gravity = gravity;
            mParams.xoff = xoff;
            mParams.yoff = yoff;
            return this;
        }

        public Builder setAlpha(float alpha) {
            mParams.alpha = alpha;
            return this;
        }


        public Builder setDismissListener(PopupWindow.OnDismissListener dismissListener) {
            mParams.mDismissListener = dismissListener;
            return this;
        }
    }
}
