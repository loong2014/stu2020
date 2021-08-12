package com.sunny.module.stu.C性能优化.A启动优化;

enum c启动方式 {
    冷启动, // 后台没有应用的进程
    热启动, // 进程还在，activity还在
    温启动 // 进程还在，activity被销毁
}
