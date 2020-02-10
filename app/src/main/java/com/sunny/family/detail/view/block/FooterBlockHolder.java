package com.sunny.family.detail.view.block;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.R;
import com.sunny.family.detail.view.IBackTopListener;
import com.sunny.family.detail.view.common.BasePosterController;
import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.Poster;
import com.sunny.family.detail.view.common.PosterInfo;

/**
 * Created by ZhangBoshi
 * on 2020-01-13
 */
public class FooterBlockHolder extends BlockHolder {
    private static final int IDX_TAB_BACK = 0;

    @Nullable
    private IBackTopListener mBackToTopListener;

    @NonNull
    private final BasePosterController mBackTab = new BasePosterController() {
        @Override
        protected void onBindView(@NonNull View v) {

        }

        @Override
        protected void onBindData(@Nullable Poster poster) {

        }
    };

    private FooterBlockHolder(@NonNull View view) {
        super(view);
        mBackTab.bindView(view.findViewById(R.id.tab_back));
    }

    public void setBackToTopListener(@Nullable IBackTopListener listener) {
        mBackToTopListener = listener;
    }

    @Override
    protected void onBindBlockData(@NonNull BlockInfo info) {
        bindControllerData(mBackTab, new Poster(), IDX_TAB_BACK);
    }

    @Override
    protected void processPosterInfo(@NonNull PosterInfo info) {
        if (info.getPoster() != null) {
            info.getPoster().setName("回到顶部");
        }
        super.processPosterInfo(info);
    }

    @Override
    protected boolean onControllerClick(@Nullable PosterInfo info) {
        if (info != null && info.getIdx() == IDX_TAB_BACK && mBackToTopListener != null) {
            mBackToTopListener.backToTop();
        }
        return true;
    }

    public static FooterBlockHolder create(@NonNull ViewGroup parent) {
        Context context = parent.getContext();
        View root = LayoutInflater.from(context).inflate(R.layout.block_footer, parent,
                false);
        return new FooterBlockHolder(root);
    }

}