package com.sunny.family.detail.view.common;


import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.lib.utils.ContextProvider;

/**
 * Created by ZhangBoshi
 */

public class BaseBlockFactory implements IBlockFactory {
    @NonNull
    @Override
    public BlockHolder createBlockHolder(ViewGroup parent, int viewType) {
        return new BlankHolder();
    }

    @Nullable
    @Override
    public IBlockAttachListener createBlockAttachListener() {
        return null;
    }

    @Nullable
    @Override
    public IBlockClickListener createBlockClickListener() {
        return null;
    }

    @Nullable
    @Override
    public IPosterInfoProcessor createPosterInfoProcessor() {
        return null;
    }

    @Nullable
    @Override
    public IBlockBindListener createBlockBindListener() {
        return null;
    }

    private static class BlankHolder extends BlockHolder {
        BlankHolder() {
            super(new FrameLayout(ContextProvider.appContext));
        }

        @Override
        protected void onBindBlockData(@NonNull BlockInfo info) {
            // do nothing
        }
    }
}
