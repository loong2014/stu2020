package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class Stu_ServiceManager extends StuImpl {

    @Override
    public void a_是什么() {
        // 整个 Binder 通信机制的管家
        /*
        Client端和Server端通信时都需要先获取Service Manager接口，才能开始通信服务
         */
    }

    @Override
    public void c_创建() {
        // 系统启动时，创建 serviceManager服务
    }
}
