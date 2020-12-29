package com.sunny.module.login;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;
import com.sunny.lib.common.service.ServiceFactory;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class ModuleLoginApplication extends BaseModuleApplication {

    @Override
    public void initModuleApp(@NotNull Application application) {
        ServiceFactory.getInstance().setAccountService(new SunAccountService());
    }

    @Override
    public void initModuleData(@NotNull Application application) {

    }
}
