package com.sunny.lib.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sunny.lib.common.utils.SunLog;

/**
 * Created by zhangxin17 on 2020/11/18
 */
public class SunnySwipeToLoadLayout extends SwipeToLoadLayout {
    private static final String TAG = "Zhang-Swipe_Layout";

    private View mTopView;

    public SunnySwipeToLoadLayout(Context context) {
        super(context);
    }

    public SunnySwipeToLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SunnySwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopView(View view) {
        mTopView = view;
    }

    @Override
    protected boolean canChildScrollUp() {
        if (isViewCovered(mTopView)) {
            SunLog.i(TAG, "canChildScrollUp  mTopView isCovered");
            return true;
        }

        return super.canChildScrollUp();
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
