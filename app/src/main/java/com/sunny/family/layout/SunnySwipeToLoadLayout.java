package com.sunny.family.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sunny.lib.utils.SunLog;

/**
 * Created by zhangxin17 on 2020/11/18
 */
public class SunnySwipeToLoadLayout extends SwipeToLoadLayout {
    private static final String TAG = "Zhang-Swipe_Layout";

    private View mFirstView;
    private RecyclerView mDataRecyclerView;

    public SunnySwipeToLoadLayout(Context context) {
        super(context);
    }

    public SunnySwipeToLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SunnySwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFirstView(View view) {
        mFirstView = view;
    }

    public void setDataRecyclerView(RecyclerView recyclerView) {
        mDataRecyclerView = recyclerView;
    }

    @Override
    protected boolean canChildScrollUp() {
        if (mDataRecyclerView != null && mDataRecyclerView.canScrollVertically(-1)) {
            SunLog.i(TAG, "canChildScrollUp  mDataRecyclerView can scroll up");
            return true;
        }
        boolean isCovered = isViewCovered(mFirstView);
        SunLog.i(TAG, "canChildScrollUp  mFirstView isCovered :" + isCovered);

        if (isCovered) {
            return true;
        }

        boolean can = super.canChildScrollUp();
        SunLog.i(TAG, "canChildScrollUp :" + can);
        return can;
    }


    private boolean isViewCovered(final View view) {
        if (view == null) {
            return false;
        }
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        // if any part of the view is clipped by any of its parents,return true
        if (!totalViewVisible)
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            // if the parent of view is not visible,return true
            if (currentParent.getVisibility() != View.VISIBLE)
                return true;

            int start = indexOfViewInParent(currentView, currentParent);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                Rect viewRect = new Rect();
                view.getGlobalVisibleRect(viewRect);
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                // if view intersects its older brother(covered),return true
                if (Rect.intersects(viewRect, otherViewRect))
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }

    private int indexOfViewInParent(View view, ViewGroup parent) {
        int index;
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == view)
                break;
        }
        return index;
    }
}
