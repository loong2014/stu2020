package com.sunny.module.stu.C性能优化;

//应用启动流程

enum c启动方式 {
    冷启动, // 后台没有应用的进程
    热启动, // 进程还在，activity还在
    温启动 // 进程还在，activity被销毁
}

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

// 设置启动窗口的主题
class A视觉优化 {

    void 透明主题() {
    /*
        <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <item name="android:windowFullscreen">true</item>
            <item name="android:windowIsTranslucent">true</item>
        </style>
    */
    }

    void 图片主题() {
    /*
        <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
            <item name="android:windowBackground">@drawable/lunch</item>  //闪屏页图片
            <item name="android:windowFullscreen">true</item>
            <item name="android:windowDrawsSystemBarBackgrounds">false</item><!--显示虚拟按键，并腾出空间-->
        </style>
     */
    }

}

class d统计启动时间 {
    void 冷启动耗时统计_adb命令() {
    /*
        adb shell am start -S -W [包名]/[启动类的全限定名] // -S 表示重启当前应用

        C:\Android\Demo>adb shell am start -S -W com.example.moneyqian.demo/com.example.moneyqian.demo.MainActivity
        Stopping: com.example.moneyqian.demo
        Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.moneyqian.demo/.MainActivity }
        Status: ok
        Activity: com.example.moneyqian.demo/.MainActivity
        ThisTime: 2247  // 最后一个 Activity 的启动耗时(例如从 LaunchActivity - >MainActivity「adb命令输入的Activity」 , 只统计 MainActivity 的启动耗时)
        TotalTime: 2247 // 启动一连串的 Activity 总耗时.(有几个Activity 就统计几个)
        WaitTime: 2278 // 应用进程的创建过程 + TotalTime
        Complete
     */
    }

    void 冷启动耗时统计_启动日志() {

        // 过滤`displayed`输出的启动日志.
    /*
        adb shell am start -S -W [包名]/[启动类的全限定名] // -S 表示重启当前应用

        C:\Android\Demo>adb shell am start -S -W com.example.moneyqian.demo/com.example.moneyqian.demo.MainActivity
        Stopping: com.example.moneyqian.demo
        Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.moneyqian.demo/.MainActivity }
        Status: ok
        Activity: com.example.moneyqian.demo/.MainActivity
        ThisTime: 2247  // 最后一个 Activity 的启动耗时(例如从 LaunchActivity - >MainActivity「adb命令输入的Activity」 , 只统计 MainActivity 的启动耗时)
        TotalTime: 2247 // 启动一连串的 Activity 总耗时.(有几个Activity 就统计几个)
        WaitTime: 2278 // 应用进程的创建过程 + TotalTime
        Complete
     */
    }
}

class A代码优化 {


    void Application() {

    }

}