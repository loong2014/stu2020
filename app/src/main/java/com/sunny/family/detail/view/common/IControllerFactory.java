package com.sunny.family.detail.view.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * Created by ZhangBoshi
 * on 2019-12-9
 */
public interface IControllerFactory {
    @NonNull
    BasePosterController createPosterController(ViewGroup viewGroup, int viewType);
}