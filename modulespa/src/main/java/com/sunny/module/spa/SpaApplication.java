package com.sunny.module.spa;

import androidx.annotation.NonNull;

import com.sunny.lib.common.base.BaseApplication;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class SpaApplication extends BaseApplication {
    @NonNull
    @Override
    public String getLogTag() {
        return "SunnySpa-";
    }
}
