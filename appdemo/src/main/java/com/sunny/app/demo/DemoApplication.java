package com.sunny.app.demo;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class DemoApplication extends BaseModuleApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化组件 Application
        doInitModuleApp(this);

        // 所有 Application 初始化后的操作
        doInitModuleData(this);
    }

    @Override
    public void initModuleApp(@NotNull Application application) {
    }

    @Override
    public void initModuleData(@NotNull Application application) {
    }
}
