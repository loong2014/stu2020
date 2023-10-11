package com.sunny.module.stu.BAndroid.G屏幕刷新机制;

import com.sunny.module.stu.base.StuImpl;

public class Stu_vsync extends StuImpl {

    // 硬件抽象层（Hardware Abstraction Layer，简称HAL）

    @Override
    public void a_是什么() {
        /*
        VSYNC信号是一个垂直同步信号，它的频率决定了屏幕的刷新率。例如，如果一个设备的刷新率是60Hz，那么VSYNC信号的频率就是60次/秒。
        当一个VSYNC信号到来时，Choreographer会执行DrawFrame任务，Surface Flinger会进行视图的合成，硬件显示器会进行图像的显示，这就完成了一次屏幕的刷新。


        Q：谁负责VSYNC的管理
        A：VSYNC信号是一个由硬件显示器产生，通过HAL传递给Surface Flinger，然后由Surface Flinger通知回调函数进行相应操作的一个信号

        Q：choreographer的作用
        A：负责处理帧同步相关的回调，它确保了整个UI系统的流畅性和响应性
            1. **帧同步和回调调度**：责监听Vsync信号，确保了所有的UI更新和动画都能够在正确的时间点执行，从而提高了渲染的效率和流畅性。
            2. **动画和滚动的时间控制**：`Choreographer`提供了时间基准，使得动画和滚动等操作能够在正确的时间进行。例如，当你使用`ObjectAnimator`或`ValueAnimator`进行动画时，这些动画的时间控制就是由`Choreographer`提供的。

        在Android的绘制过程中，有一个非常重要的概念叫做"双缓冲"。双缓冲是一种可以防止屏幕撕裂和闪烁的技术。
        在这种技术中，应用会有两个缓冲区，一个是前缓冲区，一个是后缓冲区。绘制操作都在后缓冲区进行，当所有的绘制操作完成后，
        会将后缓冲区的内容复制到前缓冲区，然后由Surface Flinger将前缓冲区的内容显示到屏幕上。
        这样，用户总是看到完整的帧，而不会看到正在绘制的帧，从而避免了屏幕撕裂和闪烁。

        Q：查看设备是不是双缓冲
        A：dumpsys SurfaceFlinger | grep  BufferQueue


        Android的屏幕刷新机制主要涉及到以下几个关键部分：应用程序、Surface Flinger、硬件显示器。这三者之间的交互过程大致如下：

        应用程序：应用程序在其UI线程中进行绘制操作，这些操作主要在一个叫做ViewRootImpl的类中进行。当所有的绘制操作完成后，会生成一个DrawFrame任务，这个任务会被提交到Choreographer中。Choreographer是Android中用来同步输入事件、动画和绘制操作的类，它会在VSYNC信号到来时执行DrawFrame任务。

        Surface Flinger：Surface Flinger是Android的一个系统服务，它负责合成所有应用和系统界面的视图，并将最后的结果显示到屏幕上。每一个应用的UI都会被绘制到一个Surface上，Surface Flinger会在VSYNC信号到来时，将所有的Surface进行合成，并将合成后的图像发送给硬件显示器。

        硬件显示器：硬件显示器会在每一个VSYNC信号到来时，将接收到的图像显示到屏幕上。

         */
    }

    void surfaceFlinger() {
        // SurfaceFlinger 是 Android 操作系统中负责管理和合成所有应用和系统表面的系统服务。
        // 它是 Android 图形架构的核心组成部分，负责从各种应用程序和系统界面接收图像，然后将这些图像合成到一个缓冲区，
        // 最后将该缓冲区的内容显示到设备的屏幕上。
        /*
        SurfaceFlinger 主要有以下几个功能：

        图形合成：SurfaceFlinger 从各个应用程序和系统界面接收图像，然后将这些图像合成到一个缓冲区。这个过程包括处理透明度、缩放和旋转等图形操作。

        管理屏幕显示：SurfaceFlinger 负责将合成后的图像显示到设备的屏幕上。它可以管理多个屏幕，包括主屏幕和外接屏幕。

        管理表面：SurfaceFlinger 通过一个叫做 Surface 的接口来管理应用程序的图像。每个应用程序都有自己的 Surface，用于绘制其图像。

        硬件加速：SurfaceFlinger 可以利用设备的 GPU 进行硬件加速，以提高图形合成的性能。
        */


    }

    void 监听屏幕刷新() {
        /*
        private var isRunning = false
        private fun doStopDebug() {
            isRunning = false
        }

        private fun doStartDebug() {
            isRunning = true
            Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
                override fun doFrame(frameTimeNanos: Long) {
                    // 在这里进行绘制操作...
                    Timber.d("PaxDebug-doFrame")

                    if (isRunning) {
                        // 如果你想在下一帧再次进行绘制，你可以再次注册这个回调
                        Choreographer.getInstance().postFrameCallback(this)
                    }
                }
            })
        }
        */
    }
}
