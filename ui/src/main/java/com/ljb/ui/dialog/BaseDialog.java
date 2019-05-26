package com.ljb.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.ljb.ui.R;


/**
 * Author      :ljb
 * Date        :2018/4/6
 * Description : 自定义的万能Dialog
 */
public class BaseDialog extends Dialog {

    private DialogController mController;

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mController = new DialogController(this, getWindow());
    }


    /**
     * 设置监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mController.setOnClickListener(viewId, listener);

    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mController.setText(viewId, text);
    }

    /**
     * 获取实例
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        return mController.getView(viewId);
    }

    public static class Builder {
        private final DialogController.DialogParams mParams;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int themeResId) {

            mParams = new DialogController.DialogParams(context, themeResId);
        }

        /**
         * 构建一个dialog
         * @return
         */
        public BaseDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final BaseDialog dialog = new BaseDialog(mParams.mContext, mParams.mThemeResId);
            mParams.apply(dialog.mController);
            dialog.setCancelable(mParams.mCancelable);
            if (mParams.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(mParams.mOnCancelListener);
            dialog.setOnDismissListener(mParams.mOnDismissListener);
            if (mParams.mOnKeyListener != null) {
                dialog.setOnKeyListener(mParams.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * 设置布局
         *
         * @param layoutResId 设置布局得Id
         * @return
         */
        public Builder setContentView(int layoutResId) {
            mParams.mView = null;
            mParams.mViewLayoutResId = layoutResId;
            return this;
        }

        /**
         * 设置布局
         *
         * @param view
         * @return
         */
        public Builder setContentView(View view) {
            mParams.mView = view;
            mParams.mViewLayoutResId = 0;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mParams.mCancelable = cancelable;
            return this;
        }


        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mParams.mOnCancelListener = onCancelListener;
            return this;
        }


        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mParams.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            mParams.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return
         */
        public Builder setText(int viewId, CharSequence text) {
            mParams.mTextArray.put(viewId, text);
            return this;
        }

        /**
         * 设置点击事件
         *
         * @param viewId
         * @param onClickListener
         * @return
         */
        public Builder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            mParams.mClickArray.put(viewId, onClickListener);
            return this;
        }

        /**
         * 设置全屏
         *
         * @return
         */
        public Builder fullScreen() {
            mParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 从底部弹出
         *
         * @param isAnimation 是否有动画
         * @return
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                mParams.mAnimation = R.style.dialog_from_bottom;
            }
            mParams.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            mParams.mWidth = width;
            mParams.mHeight = height;
            return this;
        }

        public Builder addAnimations(@IdRes int styleAnimation) {
            mParams.mAnimation = styleAnimation;
            return this;
        }

        public Builder setGravity(int gravity) {
            mParams.mGravity = gravity;
            return this;
        }

        public BaseDialog show() {
            final BaseDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}