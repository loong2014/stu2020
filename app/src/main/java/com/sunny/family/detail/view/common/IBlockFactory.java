package com.sunny.family.detail.view.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *  Created by zhoujiamu on 2018/11/13.
 * 封装了所有业务逻辑的抽象工厂
 * 业务层实现此接口, 并绑定到{@link BlockAdapter}, 将业务逻辑注册到Block框架中
 */
public interface IBlockFactory {
    @NonNull
    BlockHolder createBlockHolder(ViewGroup parent, int viewType);

    @Nullable
    IBlockClickListener createBlockClickListener();

    @Nullable
    IBlockAttachListener createBlockAttachListener();

    @Nullable
    IBlockBindListener createBlockBindListener();

    @Nullable
    IPosterInfoProcessor createPosterInfoProcessor();
}
