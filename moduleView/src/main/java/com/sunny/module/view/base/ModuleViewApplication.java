package com.sunny.module.view.base;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class ModuleViewApplication extends BaseModuleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initModuleApp(this);

        initModuleData(this);
    }

    @Override
    public void initModuleApp(@NotNull Application application) {

    }

    @Override
    public void initModuleData(@NotNull Application application) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
