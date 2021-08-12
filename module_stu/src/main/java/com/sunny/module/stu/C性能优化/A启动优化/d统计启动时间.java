package com.sunny.module.stu.C性能优化.A启动优化;

//应用启动流程

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

