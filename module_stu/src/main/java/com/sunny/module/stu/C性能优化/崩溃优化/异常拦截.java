package com.sunny.module.stu.C性能优化.崩溃优化;

import com.sunny.lib.common.crash.CrashManager;
import com.sunny.module.stu.base.StuImpl;

public class 异常拦截 extends StuImpl {
    private static final String TAG = "CrashManager";

    @Override
    public void a_是什么() {
        // 拦截系统的exception

    }

    @Override
    public void d_怎么用() {
        // 在应用启动时，初始化

        CrashManager.getInstance().init(null);
    }
}
