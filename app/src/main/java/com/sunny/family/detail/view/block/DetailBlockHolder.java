package com.sunny.family.detail.view.block;

import android.view.View;

import androidx.annotation.NonNull;

import com.sunny.family.detail.view.common.BlockHolder;
import com.sunny.family.detail.view.common.BlockInfo;
import com.sunny.family.detail.view.common.PosterInfo;
import com.sunny.lib.report.PosterInfoUtils;

/**
 * Created by ZhangBoshi
 * on 2019-12-16
 */
public abstract class DetailBlockHolder extends BlockHolder {

    public DetailBlockHolder(@NonNull View view) {
        super(view);
    }

    @Override
    protected void onBindBlockData(@NonNull BlockInfo info) {

    }

    @Override
    protected void onRecycled() {
        super.onRecycled();
    }

    @Override
    protected void processPosterInfo(@NonNull PosterInfo info) {
        // 在这里处理每个坑位的上报信息。
        PosterInfoUtils.commonProcess(info);
    }

}
