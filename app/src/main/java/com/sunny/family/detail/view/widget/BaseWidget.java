package com.sunny.family.detail.view.widget;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.detail.view.common.Poster;

/**
 * Created by ZhangBoshi 2019/2/21.
 */
public class BaseWidget {
    private int mWidgetType;
    @NonNull
    final View mRoot;

    BaseWidget(@NonNull View root) {
        mRoot = root;
    }

    public int getWidgetType() {
        return mWidgetType;
    }

    public void setWidgetType(int widgetType) {
        this.mWidgetType = widgetType;
    }

    public void addToController(@NonNull ViewGroup parent) {
        parent.addView(mRoot);
    }

    public void removeFromController(@NonNull ViewGroup parent) {
        parent.removeView(mRoot);
    }

    public void onFocusChanged(boolean hasFocus) {
        // do nothing
    }

    public void bindData(@Nullable Poster poster) {
        // do nothing
    }

    public void onViewSelect(boolean isSelect) {

    }

    @NonNull
    public View getView() {
        return mRoot;
    }
}
