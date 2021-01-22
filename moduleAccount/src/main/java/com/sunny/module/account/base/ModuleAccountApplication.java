package com.sunny.module.account.base;

import android.app.Application;

import com.sunny.lib.common.base.BaseModuleApplication;
import com.sunny.lib.common.service.ServiceFactory;
import com.sunny.module.account.SunAccountService;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zhangxin17 on 2020/12/25
 */
public class ModuleAccountApplication extends BaseModuleApplication {

    @Override
    public void initModuleApp(@NotNull Application application) {
        ServiceFactory.getInstance().setAccountService(new SunAccountService());
    }

    @Override
    public void initModuleData(@NotNull Application application) {

    }
}
