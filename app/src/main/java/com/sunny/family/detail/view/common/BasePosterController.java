package com.sunny.family.detail.view.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 * Block框架最小组成单位, 只负责ui逻辑
 */
public abstract class BasePosterController {
    @Nullable
    private Poster mPoster;
    @Nullable
    private View mView;

    /**
     * 绑定当前Controller的view布局
     */
    protected abstract void onBindView(@NonNull View v);

    /**
     * 绑定当前view对应Poster数据
     */
    protected abstract void onBindData(@Nullable Poster poster);

    @Nullable
    public final Poster getData() {
        return mPoster;
    }

    @Nullable
    public final View getView() {
        return mView;
    }

    public final void bindView(@Nullable View v) {
        if (bindViewAvailable())
            doBindView(v);
    }

    protected void doBindView(@Nullable View v) {
        mView = v;
        if (v != null)
            onBindView(v);
    }

    protected boolean bindViewAvailable() {
        return true;
    }

    public final void bindData(@Nullable Poster poster) {
        mPoster = poster;
        onBindData(poster);
    }

    public void onFocusChanged(boolean focus, @NonNull View view) {
        // do nothing
    }

    public void onViewSelect(boolean isSelect) {

    }

}