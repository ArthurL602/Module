package com.ljb.ui.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.badoo.mobile.util.WeakHandler;
import com.ljb.ui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


/**
 * Created by ljb on 2017/12/19.
 * 广告轮播（轮播到图片最后，往前轮播）
 */

public class XViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    /**
     * 图片路径
     */
    private List<String> mImageUrls;
    /**
     * 轮播的ImageView集合
     */
    private List<ImageView> mImageViews;
    private Context mContext;
    /**
     * 随图片滚动的指示器图片集合
     */
    private List<ImageView> mIndicators;
    private ViewPager mViewPager;
    /**
     * 指示器的父布局
     */
    private LinearLayout mIndicator;
    /**
     * 图片加载监听器
     */
    private ImageLoaderListener mImageLoaderListener;
    /**
     * 延迟时间【默认2500ms】
     */
    private int mDelayTime = 2500;
    private XViewPagerAdatper mAdatper;
    private WeakHandler mHandler;
    private int currentIndex;
    /**
     * 指示器的宽度
     */
    private int mDefualtIndicatorWidth;
    /**
     * 是否滑动到过最后
     */
    private boolean isLast = false;
    /**
     * 是否允许自动轮播
     */
    private boolean isAutoPlay = false;
    /**
     * 是否支持无限循环
     */
    private boolean isInfiniteLoop = false;
    private int lastPosition=1;

    public XViewPager(Context context) {
        this(context, null);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageUrls = new ArrayList<>();
        mImageViews = new ArrayList<>();
        mIndicators = new ArrayList<>();
        mHandler = new WeakHandler();
        mContext = context;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDefualtIndicatorWidth = metrics.widthPixels / 80;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mImageViews.clear();
        View view = LayoutInflater.from(context).inflate(R.layout.xviewpager, this, true);
        mViewPager = view.findViewById(R.id.xviewpager);
        mIndicator = view.findViewById(R.id.indicator);
//        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 设置图片监听器
     *
     * @param imageLoaderListener
     * @return
     */
    public XViewPager setImageLoader(ImageLoaderListener imageLoaderListener) {
        this.mImageLoaderListener = imageLoaderListener;
        return this;
    }

    /**
     * 设置图片路径
     *
     * @param imageUrls
     * @return
     */
    public XViewPager setImages(List<String> imageUrls) {
        mImageUrls = imageUrls;
        return this;
    }

    /**
     * 设置是否自动轮播
     *
     * @param isAutoPlay
     * @return
     */
    public XViewPager setAutoPlayer(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * 设置延迟
     *
     * @param delayTime
     * @return
     */
    public XViewPager setDelayTime(int delayTime) {
        mDelayTime = delayTime;
        return this;
    }

    /**
     * 开始
     *
     * @return
     */
    public XViewPager start() {
        initIndicator();
        initImages();
        setData();
        return this;
    }

    public XViewPager setFinishLoop(boolean finishLoop) {
        isInfiniteLoop = finishLoop;
        return this;
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (mAdatper == null) {
            mAdatper = new XViewPagerAdatper();
            mViewPager.addOnPageChangeListener(this);
        }
        mViewPager.setAdapter(mAdatper);
        if(isInfiniteLoop){
            mViewPager.setCurrentItem(1);
        }

        if (isAutoPlay) {
            startAutoPlay();
        }

    }

    /**
     * 开始启动轮播
     */
    public void startAutoPlay() {
        if (isAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, mDelayTime);
        }

    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        if (isAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
        }

    }

    /**
     * 释放XViewPager
     */
    public void destroyXViewPager() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 实例化广告图片
     */
    private void initImages() {
        mImageViews.clear();
        if (!isInfiniteLoop) {//不支持无限循环
            for (int i = 0; i < mImageUrls.size(); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageViews.add(imageView);
                if (mImageLoaderListener != null) {
                    mImageLoaderListener.displayView(mContext, imageView, mImageUrls.get(i));
                }
            }
        } else {//支持无限循环
            for (int i = 0; i <= mImageUrls.size() + 1; i++) {
                String url = "";
                if (i == 0) {
                    url = mImageUrls.get(mImageUrls.size() - 1);
                } else if (i == mImageUrls.size() + 1) {
                    url = mImageUrls.get(0);
                } else {
                    url = mImageUrls.get(i - 1);
                }
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageViews.add(imageView);
                if (mImageLoaderListener != null) {
                    mImageLoaderListener.displayView(mContext, imageView, url);
                }
            }

        }

    }

    /**
     * 实例化指示器
     */
    private void initIndicator() {
        mIndicators.clear();
        mIndicator.removeAllViews();
        for (int i = 0; i < mImageUrls.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDefualtIndicatorWidth,
                    mDefualtIndicatorWidth);
            params.leftMargin = 5;
            params.rightMargin = 5;
            if (i == 0) {
                imageView.setImageResource(R.drawable.gray_radius);
            } else {
                imageView.setImageResource(R.drawable.white_radius);
            }
            mIndicators.add(imageView);
            mIndicator.addView(imageView, params);
        }
    }


    private Runnable mRunnable = new TimerTask() {
        @Override
        public void run() {
            if (isAutoPlay && !isInfiniteLoop) {
                if (!isLast) {//没有滑动到过最后
                    currentIndex++;
                    if (currentIndex >= mImageViews.size() - 1) {
                        if (currentIndex >= mImageViews.size()) {
//                            Log.e("TAG", "滑动到最后的");
                            currentIndex = mImageViews.size() - 1;
                            mViewPager.setCurrentItem(currentIndex);
                            mHandler.post(mRunnable);
                        } else {
                            mViewPager.setCurrentItem(currentIndex);
                            mHandler.postDelayed(mRunnable, mDelayTime);
                        }
                        isLast = true;
                    } else {
                        mViewPager.setCurrentItem(currentIndex);
                        mHandler.postDelayed(mRunnable, mDelayTime);
                    }
                } else {//滑动到过最后
                    currentIndex--;
                    if (currentIndex <= 0) {
                        if (currentIndex <= -1) {
                            currentIndex = 0;
                            mViewPager.setCurrentItem(currentIndex);
                            mHandler.post(mRunnable);
//                            Log.e("TAG", "滑动到最前的");
                        } else {
                            mViewPager.setCurrentItem(currentIndex);
                            mHandler.postDelayed(mRunnable, mDelayTime);
                        }
                        isLast = false;
                    } else {
                        mViewPager.setCurrentItem(currentIndex);
                        mHandler.postDelayed(mRunnable, mDelayTime);
                    }
                }
//                Log.e("TAG", "currentIndex: " + currentIndex);

            }else if(isAutoPlay && isInfiniteLoop){
                currentIndex = currentIndex % (mImageUrls.size() + 1) + 1;
//                Log.e("TAG", "runnable  currentIndex: "+currentIndex);
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentIndex == 1) {
                    mViewPager.setCurrentItem(currentIndex, false);
                    mHandler.post(mRunnable);
                } else {
                    mViewPager.setCurrentItem(currentIndex);
                    mHandler.postDelayed(mRunnable, mDelayTime);
                }
            }

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
        if (!isInfiniteLoop) {
            for (int i = 0; i < mIndicators.size(); i++) {
                if (position == i) {
                    mIndicators.get(position).setImageResource(R.drawable.gray_radius);
                } else {
                    mIndicators.get(i).setImageResource(R.drawable.white_radius);
                }
            }
        } else {//支持无限滑动
            for (int i = 0; i < mIndicators.size(); i++) {
                mIndicators.get(i).setImageResource(R.drawable.white_radius);
                if(position==0){
                    mIndicators.get(mIndicators.size()-1).setImageResource(R.drawable.gray_radius);
                }else if(position==mImageViews.size()-1){
                    mIndicators.get(0).setImageResource(R.drawable.gray_radius);
                }else{
                    mIndicators.get(position-1).setImageResource(R.drawable.gray_radius);
                }
            }
//            mIndicators.get((lastPosition - 1 + mIndicators.size()) % mIndicators.size()).setImageResource(R.drawable.white_radius);
//            mIndicators.get((position - 1 + mIndicators.size()) % mIndicators.size()).setImageResource(R.drawable.gray_radius);
//            lastPosition = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (isInfiniteLoop) {//支持无限循环
            if (state == ViewPager.SCROLL_STATE_SETTLING) {
                return;
            }
            if (currentIndex == 0) {
                mViewPager.setCurrentItem(mImageViews.size() - 2, false);
            } else if (currentIndex == mImageViews.size() - 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }

    }

    class XViewPagerAdatper extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mImageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
