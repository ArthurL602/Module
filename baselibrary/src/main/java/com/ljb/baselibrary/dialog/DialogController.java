package com.ljb.baselibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Author      :ljb
 * Date        :2018/4/6
 * Description :
 */
class DialogController {
    private BaseDialog mBaseDialog;
    private Window mWindow;
    private DialogViewHelper mViewHelper;

    /**
     * 获取Dialog
     *
     * @return
     */
    public BaseDialog getDialog() {
        return mBaseDialog;
    }

    /**
     * 获取Dialog的Window
     *
     * @return
     */
    public Window getWindow() {
        return mWindow;
    }

    public DialogController(BaseDialog baseDialog, Window window) {
        this.mBaseDialog = baseDialog;
        this.mWindow = window;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        mViewHelper = viewHelper;
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnClickListener(viewId, listener);
    }
    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 获取实例
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public  <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    /**
     *  参数辅助类
     */
    public static class DialogParams {
        //上下文
        public Context mContext;
        // 弹窗样式
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable = true;
        // dialog cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        // dialog dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog 按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //显示的布局的View
        public View mView;
        //布局的layoutId
        public int mViewLayoutResId;
        //显示的文本
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        // 弹窗宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 弹窗动画
        public int mAnimation = 0;
        public int mGravity = Gravity.CENTER;
        // 弹窗高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public DialogParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param controller
         */
        public void apply(DialogController controller) {
            //1. 设置布局
            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mViewLayoutResId, mContext);
            }
            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }
            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            // 给Dialong设置布局
            controller.getDialog().setContentView(viewHelper.getContentView());
            //设置ViewHelper
            controller.setViewHelper(viewHelper);
            //2. 设置文本呢
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                controller.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //3. 设置点击
            int onClickArraySize = mClickArray.size();
            for (int i = 0; i < onClickArraySize; i++) {
                controller.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            //4. 配置自定义的效果
            Window window = controller.getWindow();
            window.setGravity(mGravity);
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);

        }
    }
}
