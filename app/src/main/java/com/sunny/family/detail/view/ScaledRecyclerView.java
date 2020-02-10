package com.sunny.family.detail.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;


public class ScaledRecyclerView extends RecyclerView {
    public ScaledRecyclerView(Context context) {
        super(context);
    }

    public ScaledRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaledRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setChildrenDrawingOrderEnabled(true);
        }
    }

    /**
     * {@link Build.VERSION_CODES#LOLLIPOP}以下, 通过改变绘制顺序避免焦点重叠问题.
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int focusedIndex = indexOfChild(getFocusedChild());
        if (focusedIndex < 0) {
            // no focus in this viewGroup
            return super.getChildDrawingOrder(childCount, i);
        }
        // draw the focused child last
        if (i < focusedIndex) {
            return i;
        }
        if (i < childCount - 1) {
            return i + 1;
        }
        return focusedIndex;
    }
}
