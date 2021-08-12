package com.sunny.module.stu.C性能优化.A启动优化;

class d冷启动流程 {
    /*
        在冷启动开始时，系统有三个任务。这些任务是：
            1. 加载并启动应用程序。
            2. 启动后立即显示应用程序空白的启动窗口。
            3. 创建应用程序进程。

        一旦系统创建应用程序进程，应用程序进程就会负责下一阶段。这些阶段是：
            1. 创建app对象.
            2. 启动主线程(main thread).
            3. 创建应用入口的Activity对象.
            4. 填充加载布局Views
            5. 在屏幕上执行View的绘制过程.measure -> layout -> draw
    */
}
