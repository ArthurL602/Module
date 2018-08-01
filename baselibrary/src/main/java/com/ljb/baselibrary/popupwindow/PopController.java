package com.ljb.baselibrary.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author      :ljb
 * Date        :2018/5/10
 * Description : 实际上对popupwindow控制的类
 */

public class PopController {
    private PopupWindow mPopupWindow;
    private Params mParams;
    private PopHelper mPopHelper;

    public void show() {
        if (mParams != null && mPopupWindow != null) {
            if (mParams.isDropDown) {
                if (Build.VERSION.SDK_INT >= 19) {
                    mPopupWindow.showAsDropDown(mParams.anchor, mParams.xoff, mParams.yoff, mParams.gravity);
                } else {
                    mPopupWindow.showAsDropDown(mParams.anchor, mParams.xoff, mParams.yoff);
                }
            } else {
                mPopupWindow.showAtLocation(mParams.parent, mParams.gravity, mParams.xoff, mParams.yoff);
            }
        }
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void setPopWindow(PopupWindow popWindow, Params params) {
        mPopupWindow = popWindow;
        mParams = params;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mPopHelper.setOnclickListener(viewId, listener);
    }

    public void setText(int viewId, CharSequence text) {
        mPopHelper.setText(viewId, text);
    }

    private void setPopHelper(PopHelper popHelper) {
        mPopHelper = popHelper;
    }

    public <T extends View> T getView(int viewId) {
        return mPopHelper.getView(viewId);
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * popupwindow 所需参数封装类
     */
    public static class Params {
        public Context mContext;
        // 布局id
        public int layoutResViewId;
        //  布局 view
        public View view;
        // 动画
        public int styleAnimation;
        // 宽
        public int width;
        // 高
        public int height;
        //点击外部是否可以消失
        public boolean cancelAble;
        // 锚点
        public View anchor;
        // x轴上的偏移
        public int xoff;
        // y轴上的偏移
        public int yoff;
        // 位置
        public int gravity = Gravity.TOP | Gravity.START;
        // 父布局
        public View parent;
        //显示的文本
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //显示的文本
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        // 设置窗口透明度
        public float alpha = 1.0f;
        public PopupWindow.OnDismissListener mDismissListener;
        public boolean isDropDown;
        public Window window;

        /**
         * 参数和popupwindow进行绑定
         *
         * @param popController
         */
        public void apply(PopController popController) {
            PopHelper popHelper = null;
            if (layoutResViewId != 0) {
                popHelper = new PopHelper(layoutResViewId, mContext);
            }
            if (view != null) {
                popHelper = new PopHelper();
                popHelper.setContentView(view);
            }
            if (popHelper == null) {
                throw new IllegalArgumentException("pophelper not null");
            }
            // 设置布局文本
            for (int i = 0; i < mTextArray.size(); i++) {
                popHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            // 设置点击事件
            for (int i = 0; i < mClickArray.size(); i++) {
                popHelper.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
            //设置ViewHelper
            popController.setPopHelper(popHelper);
            // 配置自定义效果
            PopupWindow popupWindow = new PopupWindow();
            // 设置布局
            popupWindow.setContentView(popHelper.getContentView());
            // 添加动画
            if (styleAnimation != 0) {
                popupWindow.setAnimationStyle(styleAnimation);
            }
            // 必须设置宽高
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            // 处理软键盘遮盖问题
            popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            // 如果pop中有edittext,必须设置focusable为true
            popupWindow.setFocusable(true);
            //点击pop外区域 pop消失，必须结合setBackgroundDrawable()一起使用
            popupWindow.setOutsideTouchable(cancelAble);

            final Window window = this.window;
            final WindowManager.LayoutParams attr = window.getAttributes();
            attr.alpha = alpha;
            window.setAttributes(attr);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if(mDismissListener==null) return;
                    attr.alpha = 1.0f;
                    window.setAttributes(attr);
                    mDismissListener.onDismiss();
                }
            });
            popController.setPopWindow(popupWindow, this);
        }
    }


}
