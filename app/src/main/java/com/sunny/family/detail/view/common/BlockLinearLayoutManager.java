package com.sunny.family.detail.view.common;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 这个基类是为了让桌面在长按事件中实现匀速滚动而写的。
 * 使用方法只需要让桌面的 LinearLayoutManager 继承此类即可，
 * 上滚下滚的速度均可以设置，通过 {@link BlockLinearLayoutManager#setScrollingSpeed(float)}
 * 方法设置速度。
 */

public class BlockLinearLayoutManager extends LinearLayoutManager {
    private static final float SCROLLING_SPEED_INIT = 0.3f;
    private boolean isSlowFlag;
    private float mScrollSpeed;

    public BlockLinearLayoutManager(Context context) {
        super(context);
        init();
    }

    public BlockLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public BlockLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                    int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScrollSpeed = SCROLLING_SPEED_INIT;
    }

    /**
     * 此方法主要是区分"返回键"滚动和按"上"键滚动的区别。两者均是滚动到 positon = 0 的位置，不同的是，返回键传
     * isSlow=false，而"上"键需要传 isSlow=True.
     *
     * @param recyclerView
     * @param state
     * @param position
     * @param isSlow
     */
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position, boolean isSlow) {
        this.isSlowFlag = isSlow;
        smoothScrollToPosition(recyclerView, state, position);
        this.isSlowFlag = false;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller;
        if (position != 0) {
            smoothScroller = new BlockSmoothScroller(recyclerView.getContext());
        } else {
            if (isSlowFlag) {
                smoothScroller = new BlockSmoothScroller(recyclerView.getContext());
            } else {
                smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return BlockLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }
                };
            }
        }
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

    }

    public void setScrollingSpeed(float speed) {
        mScrollSpeed = speed;
    }

    private class BlockSmoothScroller extends LinearSmoothScroller {

        public BlockSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public PointF computeScrollVectorForPosition(int i) {
            return BlockLinearLayoutManager.this.computeScrollVectorForPosition(i);
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return mScrollSpeed;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return LinearSmoothScroller.SNAP_TO_START;
        }
    }
}
