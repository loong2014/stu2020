/*
 * ***********************************************************
 * File：DataBoundViewHolder.java  Module：app  Project：PaxLauncher
 * Current Modifier：2019-01-17 16:07:56
 * Author: WangYao
 * http://pax.leautolink.com
 * Copyright (c) 2018
 * ***********************************************************
 *
 */

package com.sunny.lib.common.adapter;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A generic ViewHolder that works with a {@link ViewDataBinding}.
 * @param <T> The type of the ViewDataBinding.
 */
public class DataBoundViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;
    public DataBoundViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
