package com.sunny.module.weather.base;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Created by zhangxin17 on 2020/12/25
 */
@HiltAndroidApp
public class ModuleWeatherApplication extends BaseModuleApplication {

    @Override
    public void initModuleApp(@NotNull Application application) {

    }

    @Override
    public void initModuleData(@NotNull Application application) {

    }
}
