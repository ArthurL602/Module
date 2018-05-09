package com.ljb.baselibrary.base.recyclerview.dragandswipe;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Author      :meloon
 * Date        :2018/3/29
 * Description : 简单的演示RecyclerView实现拖拽和侧滑删除
 */

public class SimpleDemo {


    public void test() {
        final List<String> mData = null;
        final RecyclerView.Adapter adapter = null;
        RecyclerView mRvTest = null;

        // RecylerView 设置拖拽
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            /**
             * 控制滑动方向
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    /*控制拖拽方向*/
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                            ItemTouchHelper.RIGHT;
                    /*设置滑动方向*/
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            /**
             * 拖动的时候回调,需要在这里处理拖动的item和集合中的item的交换，然后通知adapter刷新
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                    .ViewHolder target) {
                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //得到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mData, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mData, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            /**
             * 滑动的时候回调，做清除数据和更新RecyclerView处理
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                mData.remove(pos);
                adapter.notifyItemRemoved(pos);
            }

            /**
             * 长按选中item的时候调用，用来给选中的item的高亮
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    //可以设置用来设置高亮
//                    viewHolder.itemView.setBackgroundColor(Color.RED);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            /**
             * 松手的时候回调，高亮还原
             *
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setAlpha(1);
                viewHolder.itemView.setScaleX(1);
                viewHolder.itemView.setScaleY(1);
                //可以还原高亮
//                viewHolder.itemView.setBackgroundColor(Color.BLUE);
            }

            /**
             * item拖动或者滑动的时候回调，用来绘制Item的
             * @param c
             * @param recyclerView
             * @param viewHolder
             * @param dX X轴的偏移量
             * @param dY dY代表的是Y轴的偏移量
             * @param actionState 当前item的状态
             * @param isCurrentlyActive
             */
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float
                    dX, float dY, int actionState, boolean isCurrentlyActive) {
                // 侧滑状态
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    // 百分比
                    float ratio = 1 - Math.abs(dX) / itemView.getWidth();
                    itemView.setAlpha(ratio);
                    itemView.setScaleX(ratio);
                    itemView.setScaleY(ratio);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        });
        itemTouchHelper.attachToRecyclerView(mRvTest);
    }
}
