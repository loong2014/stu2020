package com.sunny.module.stu.BAndroid.ABC常用名词;

import android.os.SystemClock;

import com.sunny.module.stu.base.StuImpl;

public class Stu_SystemClock extends StuImpl {
    @Override
    public void a_是什么() {
        //

        long time = SystemClock.uptimeMillis();
        /*
        2、uptimeMillis()
        表示自系统启动时开始计数，以毫秒为单位。
        返回的是从系统启动到现在这个过程中的处于非休眠期的时间。

        当系统进入深度睡眠时(CPU关闭，设备变黑，等待外部输入装置)该时钟会停止。
        但是该时钟不会被时钟调整，闲置或其他节能机所影响。
        这是大多数间隔时间的基本点，例如Thread.sleep(millls)、Object.wait(millis)和System.nanoTime()。
        该时钟被保证是单调的，适用于检测不包含休眠的间隔时间的情况。大多数的方法接受一个时间戳的值除了uptimeMillis()时钟。

        3、elapsedRealtime() and elapsedRealtimeNanos()
        返回系统启动到现在的时间，包含设备深度休眠的时间。
        该时钟被保证是单调的，即使CPU在省电模式下，该时间也会继续计时。
        该时钟可以被使用在当测量时间间隔可能跨越系统睡眠的时间段。

1、标准的方法像Thread.sleep(millis) 和 Object.wait(millis)总是可用的，这些方法使用的是uptimeMillis()时钟，如果设备进入深度休眠，剩余的时间将被推迟直到系统唤醒。这些同步方法可能被Thread.interrupt()中断，并且你必须处理InterruptedException异常。
2、SystemClock.sleep(millis)是一个类似于Thread.sleep(millis)的实用方法，但是它忽略InterruptedException异常。使用该函数产生的延迟如果你不使用Thread.interrupt()，因为它会保存线程的中断状态。

3、Handler可以在一个相对或者绝对的时间设置异步回调，Handler类对象也使用uptimeMillis()时钟，而且需要一个loop(经常出现在GUI程序中)。

4、AlarmManager可以触发一次或重复事件，即使设备深度休眠或者应用程序没有运行。事件可以选择用 currentTimeMillis或者elapsedRealtime()(ELAPSED_REALTIME)来设置时间，当事件发生会触发一个广播。

         */
    }

    @Override
    public void d_怎么用() {
        /*
1、public static long currentThreadTimeMillis ()   返在当前线程运行的毫秒数。
6、public static long uptimeMillis ()   返回系统启动到现在的毫秒数，不包含休眠时间。就是说统计系统启动到现在的非休眠期时间。

2、public static long elapsedRealtime ()   返回系统启动到现在的毫秒数，包含休眠时间。

3、public static long elapsedRealtimeNanos ()   返回系统启动到现在的纳秒数，包含休眠时间。

4、public static boolean setCurrentTimeMillis (long millis)    设置当前的"墙"时间，要求调用进程有许可权限。返回是否成功。

5、public static void sleep (long ms)    等待给定的时间。和Thread.sleep(millis)类似，但是它不会抛出InterruptedException异常。事件被推迟到下一个中断操作。该方法直到指定的时间过去才返回。
         */
    }
}
