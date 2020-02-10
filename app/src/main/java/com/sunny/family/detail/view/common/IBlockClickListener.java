package com.sunny.family.detail.view.common;


import androidx.annotation.NonNull;

/**
 * Created by zhoujiamu on 2018/11/13.
 */
public interface IBlockClickListener {
    void onControllerClick(@NonNull PosterInfo info);

    void doControllerClickReport(@NonNull PosterInfo info);
}
