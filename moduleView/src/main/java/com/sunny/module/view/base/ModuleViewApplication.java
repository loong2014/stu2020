package com.sunny.module.view.base;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;

import org.jetbrains.annotations.NotNull;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class ModuleViewApplication extends BaseModuleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initModuleApp(this);

        initModuleData(this);

        // 初始化 AutoSize
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(true) // 根据宽度适配，默认是 true
                .setDesignWidthInDp(360); // 设计图的宽度，单位 dp
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
