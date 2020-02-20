package com.sunny.family.detail.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.family.detail.view.common.BlockLinearLayoutManager;
import com.sunny.lib.utils.HandlerUtils;
import com.sunny.lib.utils.SunLog;

import java.util.Locale;

/**
 * Created by ZhangBoshi
 * 封装了跟随焦点滚动逻辑
 * 除了header不能出现超过一屏的子view.
 */
public class VerticalLinearLayoutManager extends BlockLinearLayoutManager {
    private static final String TAG = "VerticalLinearLayoutManager";
    private static final String FORMAT_LOG = "position: %d, case: %s, scroll: %d";

    private static final int EXTRA_FOR_TRACK_FOCUS = 80;
    private static final int FIXED_LOCATION_BLOCK = 100;
    private HeaderBLockListener mHeaderBlockListener;


    public VerticalLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * 当子View获取焦点时会被回调, 用于实现跟随焦点滚动逻辑
     */
    @Override
    public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent, @NonNull View child,
                                                 @NonNull Rect rect, boolean immediate, boolean focusedChildVisible) {

        int scroll = 0;
        if (child.getLayoutParams() instanceof RecyclerView.LayoutParams) {
            scroll = calculateScrollDistance(parent, child, rect);
        }
        if (scroll != 0) {
            if (immediate) {
                parent.scrollBy(0, scroll);
            } else {
                parent.smoothScrollBy(0, scroll);
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
        int scroll;
        String caseStr;

        // 0.1 parent params
        int parentTop = getPaddingTop();
        int parentBottom = getHeight() - getPaddingBottom();
        int parentHeight = getHeight();

        // 0.2 block params
        int blockPosition = getPosition(child);
        int blockTop = child.getTop();
        int blockBottom = child.getBottom();
        if (child.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child
                    .getLayoutParams();
            blockTop -= params.topMargin;
            blockBottom += params.bottomMargin;
        }
        int blockHeight = blockBottom - blockTop;

        // 0.3 focus params
        int focusTop = child.getTop() + rect.top;
        int focusBottom = focusTop + rect.height();

        // calculate scroll
        // 1 header
        if (blockPosition == 0 && blockHeight < parentHeight) {
            scroll = blockTop;
            caseStr = "header";
            if (scroll < 0) {
                headerBlockShow();
            }
        }
        // 从header向下滑动，将header全部移出
        else if (blockPosition == 1 && blockTop >= 0) {
            scroll = blockTop;
            caseStr = "header_out";
            headerBlockHide();
        }
        // block3向block2移动的时候，不能显示出Header
        else if (blockPosition == 1) {
            scroll = blockTop;
            caseStr = "header_no_in";
        }
        // 2 footer
        else if (blockPosition == getItemCount() - 1 && blockHeight < parentHeight) {
            scroll = blockBottom - parentBottom;
            caseStr = "footer";
        }
        // 3.1 吸顶
        else if (blockHeight > (0.7 * parentHeight)) {
            caseStr = "align top";
            scroll = blockTop;
        }
        // 3.1 版块固定位置
        else if (blockHeight > (0.4 * parentHeight)) {
            caseStr = "track block";
            scroll = blockTop - FIXED_LOCATION_BLOCK;
        }
        // 3.3 跟踪焦点
        else {
            caseStr = "track focus";
            int offScreenTop = calculateOffScreenTop(focusTop, parentTop);
            int offScreenBottom = calculateOffScreenBottom(focusBottom, parentBottom);

            // Favor bringing the top into view over the bottom. If top is already visible and
            // we should scroll to make bottom visible, make sure top does not go out of bounds.
            scroll = offScreenTop != 0 ? offScreenTop
                    : Math.min(focusTop - parentTop, offScreenBottom);

            // Add extra scrolling dy to ensure that next view being inflated before next focus
            // search.
            if (scroll > 0) { // scroll down
                scroll += EXTRA_FOR_TRACK_FOCUS;
            } else if (scroll < 0) { // scroll up
                scroll -= EXTRA_FOR_TRACK_FOCUS;
            }
        }

        SunLog.INSTANCE.i(TAG, String.format(Locale.US, FORMAT_LOG, blockPosition, caseStr, scroll));
        return scroll;
    }

    private int calculateOffScreenTop(int childTop, int parentTop) {
        return Math.min(0, childTop - parentTop);
    }

    private int calculateOffScreenBottom(int childBottom, int parentBottom) {
        return Math.max(0, childBottom - parentBottom);
    }

    public void setmHeaderBlockListener(HeaderBLockListener mHeaderBlockListener) {
        this.mHeaderBlockListener = mHeaderBlockListener;
    }

    private void headerBlockShow() {
        if (mHeaderBlockListener != null) {
            HandlerUtils.INSTANCE.getUiHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHeaderBlockListener != null) {
                        mHeaderBlockListener.headerBlockShow();
                    }
                }
            }, 220);
        }
    }

    private void headerBlockHide() {
        if (mHeaderBlockListener != null) {
            mHeaderBlockListener.headerBlockHide();
        }

    }

}