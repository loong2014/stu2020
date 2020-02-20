package com.sunny.family.detail;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.IBlockAttachListener;
import com.sunny.family.detail.view.common.IBlockBindListener;
import com.sunny.family.detail.view.common.IBlockClickListener;
import com.sunny.family.detail.view.common.IBlockFactory;
import com.sunny.family.detail.view.common.IPosterInfoProcessor;

/**
 * Created by ZhangBoshi
 */
public class SunDetailBlockFactory implements IBlockFactory {

    @NonNull
    @Override
    public BlockHolder createBlockHolder(ViewGroup parent, int viewType) {
        return SunBlockHolderOld.create(parent);
    }

    @Nullable
    @Override
    public IBlockClickListener createBlockClickListener() {
        return null;
    }

    @Nullable
    @Override
    public IBlockAttachListener createBlockAttachListener() {
        return null;
    }

    @Nullable
    @Override
    public IBlockBindListener createBlockBindListener() {
        return null;
    }

    @Nullable
    @Override
    public IPosterInfoProcessor createPosterInfoProcessor() {
        return null;
    }
}
