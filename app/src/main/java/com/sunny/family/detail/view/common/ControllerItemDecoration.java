package com.sunny.family.detail.view.common;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ZhangBoshi
 */
public class ControllerItemDecoration extends RecyclerView.ItemDecoration {

    private int left, right, top, bottom;

    /**
     * RecyclerView 间距
     */
    public ControllerItemDecoration(int top, int bottom, int left, int right) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void setDecoration(int top, int bottom, int left, int right) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        outRect.set(left, top, right, bottom);
    }
}
