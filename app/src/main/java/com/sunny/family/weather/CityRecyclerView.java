package com.sunny.family.weather;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CityRecyclerView extends RecyclerView {

    public CityRecyclerView(@NonNull Context context) {
        this(context, null, 0);
    }

    public CityRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CityRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setChildrenDrawingOrderEnabled(true);
        }
    }

    /**
     * {@link Build.VERSION_CODES#LOLLIPOP}以下，通过改变绘制顺序避免焦点重叠问题
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
