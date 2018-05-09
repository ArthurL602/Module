package com.ljb.baselibrary.base.recyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ljb.baselibrary.base.recyclerview.animation.BaseAnimation;
import com.ljb.baselibrary.base.recyclerview.animation.ScaleInAnimation;
import com.ljb.baselibrary.base.recyclerview.animation.SlideInLeftAnimation;
import com.ljb.baselibrary.base.recyclerview.animation.SlideInRightAnimation;
import com.ljb.baselibrary.base.recyclerview.loadmore.LoadMoreView;
import com.ljb.baselibrary.base.recyclerview.loadmore.SimpleLoadMoreView;

import java.util.List;

/**
 * RecyclerView 的适配器的 基类
 */

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<BaseRvViewHolder> {

    /**
     * 空布局
     */
    public static final int TYPE_EMPTY_VIEW = 1000001;
    /**
     * 普通View
     */
    public static final int TYPE_COMMON_VIEW = 1000002;
    /**
     * 尾部布局
     */
    public static final int TYPE_LOAD_VIEW = 1000003;
    private List<T> mDatas;
    private int mLayoutId;
    private Context mContext;
    private LayoutInflater mInflater;

    private View mEmptyView;
    private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private OnItemChildClickListener mItemChildClickListener;
    private onLoadMoreListener mLoadMoreListener;


    /**
     * 是否显示Item动画
     */
    private boolean mAnimationEnalbe;
    /**
     * 默认的item加载动画
     */
    private BaseAnimation mAnimation = new SlideInLeftAnimation();

    /**
     * 动画持续时间
     */
    private long mDuration = 300;
    /**
     * 动画插值器
     */
    private Interpolator mInterpolator = new LinearInterpolator();
    /**
     * 是否第一次加载才显示动画
     */
    private boolean isFirstOnlyEnable = true;

    /**
     * 上一次加载动画的位置
     */
    private int mLastPosition = -1;
    /**
     * 是否自动加载更多
     */
    private boolean mOpenLoadMore;


    /**
     * end后是否继续加载
     */
    private boolean mEnableEndLoadMore;

    public void setEnableEndLoadMore(boolean enableEndLoadMore) {
        mEnableEndLoadMore = enableEndLoadMore;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void isFirstOnlyEnable(boolean firstOnlyEnable) {
        isFirstOnlyEnable = firstOnlyEnable;
    }

    public OnItemChildClickListener getItemChildClickListener() {
        return mItemChildClickListener;
    }

    public void setItemChildClickListener(OnItemChildClickListener itemChildClickListener) {
        mItemChildClickListener = itemChildClickListener;
    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
        mOpenLoadMore = true;
    }

    public OnItemChildLongClickListener getItemChildLongClickListener() {
        return mItemChildLongClickListener;
    }

    public void setItemChildLongClickListener(OnItemChildLongClickListener itemChildLongClickListener) {
        mItemChildLongClickListener = itemChildLongClickListener;
    }

    private OnItemChildLongClickListener mItemChildLongClickListener;

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public BaseRvAdapter(@NonNull List<T> datas, int layoutId, Context context) {
        mDatas = datas;
        mLayoutId = layoutId;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRvViewHolder holder = null;
        switch (viewType) {
            case TYPE_EMPTY_VIEW:
                holder = new BaseRvViewHolder(mEmptyView);
                break;
            case TYPE_COMMON_VIEW:
                View view = mInflater.inflate(mLayoutId, parent, false);
                holder = new BaseRvViewHolder(view);
                break;
            case TYPE_LOAD_VIEW:
                holder = getLoadView(parent);
                break;
        }
        holder.setAdapter(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, final int position) {
        if (holder.getItemViewType() == TYPE_COMMON_VIEW) {//如果是普通View
            T t = mDatas.get(position);
            convert(holder, t, position);
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(position, v, BaseRvAdapter.this);
                    }
                });
            }
            if (mItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mItemLongClickListener.onItemLongClick(position, v, BaseRvAdapter.this);
                    }
                });
            }
        } else if (holder.getItemViewType() == TYPE_LOAD_VIEW) {
            mLoadMoreView.convert(holder);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int lastVisibleItemPosition = findLastVisibleItemPosition(layoutManager);
                //判断是否加载更多
                if (mOpenLoadMore) {
                    if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount()
                            - 1 && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                        if (mLoadMoreView != null && mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_DEFAULT) {
                            mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                            notifyItemChanged(getItemCount() - 1);
                            mLoadMoreListener.onLoadMore();
                        }
                    }
                }
            }
        });

    }

    /**
     * 返回最后一个可见Item的position
     *
     * @param layoutManager
     * @return
     */
    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = findMax(into);
        } else {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public void setLoadEnd() {
        if (mLoadMoreView == null || !mOpenLoadMore) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_END);
        notifyDataSetChanged();
        notifyItemChanged(getItemCount() - 1);
    }

    public void setLoadComplete() {
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_DEFAULT);
        notifyItemChanged(getItemCount() - 1);
    }

    public void setLoadFail() {
        if (mLoadMoreView == null || !mOpenLoadMore) {
            return;
        }
        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_FAIL);
        notifyItemChanged(getItemCount() - 1);
    }

    public void setLoadMoerView(LoadMoreView loadMoerView) {
        mLoadMoreView = loadMoerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty() && mEmptyView != null) {
            return TYPE_EMPTY_VIEW;
        }
        if (isFooterView(position)) {
            return TYPE_LOAD_VIEW;
        }
        return TYPE_COMMON_VIEW;
    }

    /**
     * 判断是否是FooterView
     *
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return mOpenLoadMore && getItemCount() > 1 && position >= getItemCount() - 1 && mLoadMoreView != null;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null && mEmptyView != null || (mDatas != null && mDatas.size() == 0 && mEmptyView != null)) {
            return 1;
        } else {
            return mDatas != null ? mDatas.size() + getFooterViewCount() : 0;
        }

    }

    private int getFooterViewCount() {
        return mOpenLoadMore && mLoadMoreView != null ? 1 : 0;
    }

    public abstract void convert(BaseRvViewHolder holder, T data, int position);

    @Override
    public void onViewAttachedToWindow(BaseRvViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.getItemViewType() == TYPE_COMMON_VIEW) {
            addAnimation(holder);
        }

    }

//---------------------------------Item加载动画----------------------------------------

    public void openItemAnimatioin() {
        mAnimationEnalbe = true;
    }

    public void openItemAnimatioin(AnimationType type) {
        mAnimationEnalbe = true;
        switch (type) {
            case SLIDE_LEFT:
                mAnimation = new SlideInLeftAnimation();
                break;
            case SLIDE_RIGHT:
                mAnimation = new SlideInRightAnimation();
                break;
            case SCALE_IN:
                mAnimation = new ScaleInAnimation();
                break;
            default:
                break;
        }

    }

    public void openItemAnimatioin(BaseAnimation baseAnimation) {
        mAnimation = baseAnimation;
        mAnimationEnalbe = true;
    }

    /**
     * 添加布局加载动画
     *
     * @param holder
     */
    private void addAnimation(BaseRvViewHolder holder) {
        if (mAnimationEnalbe) {//如果可以显示Item动画
            if (!isFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                for (Animator animator : mAnimation.getAnimators(holder.itemView)) {
                    startAnimation(animator, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }

        }
    }

    /**
     * 设置第一次进入界面不需要加载动画的条目数
     *
     * @param count
     */
    public void setDoNotAnimation(int count) {
        mLastPosition = count;

    }

    private void startAnimation(Animator animator, int index) {
        animator.setDuration(mDuration).start();
        animator.setInterpolator(mInterpolator);
    }


    //------------------------------------数据的一些列增删操作--------------------------------------------

    /**
     * 移除所有数据
     */
    public void removeAllDatas() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加更多数据
     *
     * @param data
     */
    public void addMoreData(@NonNull List<T> data) {
        int size = mDatas.size();
        mDatas.addAll(data);
        notifyItemInserted(size);
    }

    /**
     * 删除某个位置的数据
     *
     * @param positon
     */
    public void removeData(int positon) {
        mDatas.remove(positon);
        notifyItemRemoved(positon);
    }

    /**
     * 在某个位置添加数据
     *
     * @param position
     * @param data
     */
    public void addDataInPosition(int position, @NonNull List<T> data) {
        mDatas.addAll(position, data);
        notifyDataSetChanged();

    }

    /**
     * 设置新数据，即删除现有的数据，添加新的数据
     *
     * @param datas
     */
    public void setNewData(@NonNull List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public BaseRvViewHolder getLoadView(ViewGroup parent) {
        View view = mInflater.inflate(mLoadMoreView.getLoadView(), parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_FAIL) {
                    if (mLoadMoreListener != null) {
                        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                        notifyItemChanged(getItemCount() - 1);
                        mLoadMoreListener.onLoadMore();
                    }
                }
                if ( mEnableEndLoadMore &&mLoadMoreView.getLoadMoreStatus() == LoadMoreView.STATUS_END) {
                    if (mLoadMoreListener != null) {
                        mLoadMoreView.setLoadMoreStatus(LoadMoreView.STATUS_LOADING);
                        notifyItemChanged(getItemCount() - 1);
                        mLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
        BaseRvViewHolder holder = new BaseRvViewHolder(view);
        return holder;
    }


//-----------------------------------一些列监听事件-----------------------------------------------

    /**
     * 点击监听器
     */

    public interface OnItemClickListener {
        void onItemClick(int position, View view, BaseRvAdapter adapter);
    }

    /**
     * 监听长按事件
     */

    public interface OnItemLongClickListener {

        boolean onItemLongClick(int position, View view, BaseRvAdapter adapter);
    }

    /**
     * 子条目的子View的点击事件监听器
     */
    public interface OnItemChildClickListener {
        void onItemChildClick(int position, View view, BaseRvAdapter adapter);
    }

    /**
     * 子条目的子View的长按事件监听器
     */
    public interface OnItemChildLongClickListener {
        boolean onItenChildLongClick(int position, View view, BaseRvAdapter adapter);
    }

    /**
     * 加载更多监听器
     */
    public interface onLoadMoreListener {
        void onLoadMore();
    }

    //---------------------------------item加载动画类型-------------------------------------------
    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, SCALE_IN;

    }
}
