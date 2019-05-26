package com.ljb.ui.recyclerview.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author      :ljb
 * Date        :2018/3/29
 * Description : 下拉刷新RecyclerView
 */

public class RefreshRecyclerView extends WrapRecyclerView {
    // 默认状态
    public static final int REFRESH_STATUS_NORMAL = 0x0000001;
    // 下拉刷新状态
    public static final int REFRESH_STATUS_PULL = 0x0000002;
    // 正在刷新状态
    public static final int RREFRESH_STATUS_REFRESHING = 0x0000003;
    // 松开刷新状态
    public static final int REFRESH_STATUS_LOOSEN = 0x0000004;
    private RefreshViewCreator mRefreshViewCreator;
    //下拉刷新View
    private View mRefreshView;
    private int mRefreshViewHeight;
    //手指摁下的y坐标
    private int mFingerDownY;
    //当前刷新状态
    private int mCurrentStatus;
    // 手指拖拽的阻力指数
    private float mDragIndex = 0.35f;
    // 是否在拖拽
    private boolean mCurrentDrag;
    //是否已经摁下
    private boolean isTouch;

    // 处理刷新回调监听
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }


    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    /**
     * 添加下拉刷新回调接口
     *
     * @param refreshViewCreator
     */
    public void addRefreshCreator(RefreshViewCreator refreshViewCreator) {
        mRefreshViewCreator = refreshViewCreator;
        if (mRefreshViewCreator != null) {
            addRefreshView();
        }
    }

    /**
     * 添加头部
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (mRefreshViewCreator != null && adapter != null) {
            View view = mRefreshViewCreator.getRefreshView(getContext(), this);
            if (view == null) return;
            addHeaderView(view);
            this.mRefreshView = view;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (canScrollUp() || mCurrentStatus == RREFRESH_STATUS_REFRESHING) {
                    return super.onTouchEvent(ev);
                }
                int distance = (int) ((ev.getRawY() - mFingerDownY) * mDragIndex);
                if (distance > 0) {
                    int marginTop = distance - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateStatus(marginTop);
                    mCurrentDrag = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前刷新状态
     */
    private void restoreRefreshView() {
        //获取当前的marginTop值
        int currentMarginTop = ((ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        //最终的marginTop值
        int finalMarginTop = -mRefreshViewHeight + 1;
        if (mCurrentStatus == REFRESH_STATUS_LOOSEN) {
            finalMarginTop = 0;
            mCurrentStatus = RREFRESH_STATUS_REFRESHING;
            if (mRefreshViewCreator != null) {
                mRefreshViewCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();

            }

        }
        int distance = currentMarginTop - finalMarginTop;
        ValueAnimator animator = ValueAnimator.ofInt(currentMarginTop, finalMarginTop);
        animator.setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int marginTop = (int) animation.getAnimatedValue();
                setRefreshViewMarginTop(marginTop);
            }
        });
        animator.start();
        mCurrentDrag = false;

    }

    /**
     * 更新刷新状态
     *
     * @param marginTop
     */
    private void updateStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentStatus = REFRESH_STATUS_PULL;
        } else {
            mCurrentStatus = REFRESH_STATUS_LOOSEN;
        }


        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onPull(marginTop, mRefreshViewHeight, mCurrentStatus);

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    public boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 设置下拉刷新View的marginTop
     *
     * @param marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        layoutParams.topMargin = marginTop;
        mRefreshView.setLayoutParams(layoutParams);
    }
    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onStopRefresh();
        }
    }


    public interface OnRefreshListener {
        void onRefresh();
    }

}
