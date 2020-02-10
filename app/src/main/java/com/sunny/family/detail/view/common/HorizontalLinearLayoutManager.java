package com.sunny.family.detail.view.common;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.lib.utils.SunLog;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 * 封装了跟随焦点滚动逻辑
 * 除了header不能出现超过一屏的子view.
 */
public class HorizontalLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "VerticalLinearLayoutManager";
    private static final String FORMAT_LOG = "position: %d, case: %s, scroll: %d";

    private static final int EXTRA_FOR_TRACK_FOCUS = 80;
    private static final int FIXED_LOCATION_BLOCK = 100;
    private float mScrollSpeed = 0.5f;

    HorizontalLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * 当子View获取焦点时会被回调, 用于实现跟随焦点滚动逻辑
     */
    @Override
    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child,
                                                 @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {

        int scroll = calculateScrollDistance(parent, child, rect);
        if (scroll != 0) {
            if (immediate) {
                parent.scrollBy(scroll, 0);
            } else {
                parent.smoothScrollBy(scroll, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        int scrolled = 0;
        try {
            scrolled = super.scrollVerticallyBy(dy, recycler, state);

        } catch (IndexOutOfBoundsException e) {
            SunLog.INSTANCE.e(TAG, "IndexOutOfBoundsException caught!");
            e.printStackTrace();
        }

        return scrolled;
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);

    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(
                recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);

    }

    public void setScrollingSpeed(float speed) {
        mScrollSpeed = speed;
    }

    private class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd,
                                    int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }

//        @Override
//        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//            return mScrollSpeed;
//        }
    }

    /**
     * 遗留逻辑, 不知场景, 暂时保留
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            SunLog.INSTANCE.e(TAG, "IndexOutOfBoundsException caught!");
            e.printStackTrace();
        }
    }

    /**
     * 0. 准备阶段
     * 1. header: 优先滚动到头部位置(要减掉recycler view的paddingTop), 若获得焦点的View无法展示完全, 则跟踪焦点滚动.
     * 2. footer: 滚动到最底部
     * 3. 根据block的高度判断
     * -- 3.1 若大于父布局的0.7, 则吸顶
     * -- 3.2 版块固定位置
     * -- 3.3 跟踪焦点
     */
    private int calculateScrollDistance(@NonNull RecyclerView parent, @NonNull View child,
                                        @NonNull Rect rect) {
        // 0. prepare
        int scroll = 0;
        String caseStr;

        // 0.1 parent params
        int parentWidth = getWidth();

        // 0.2 block params
        int itemStart = child.getLeft();
        int itemEnd = child.getRight();

        if (child.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child
                    .getLayoutParams();
            itemStart -= params.leftMargin;
            itemEnd += params.rightMargin;
        }
        int itemWidth = itemEnd - itemStart;

        // 0.3 focus params

        int childCount = getChildCount();

        int midPosition = childCount / 2;
        int midWidth = parentWidth / 2;
        int midItemWidth = itemWidth / 2;

        scroll = itemStart - midWidth + midItemWidth;

        return scroll;
    }

    private int calculateOffScreenTop(int childTop, int parentTop) {
        return Math.min(0, childTop - parentTop);
    }

    private int calculateOffScreenBottom(int childBottom, int parentBottom) {
        return Math.max(0, childBottom - parentBottom);
    }

}