package com.sunny.module.stu.BAndroid.调试;

import com.sunny.module.stu.base.StuImpl;

/**
 * https://blog.csdn.net/mengfeicheng2012/article/details/53768954
 */
public class Stu帧率统计 extends StuImpl {

    @Override
    public void a_是什么() {
        // adb shell dumpsys gfxinfo com.letv.tv
        /*
         命令行运行adb shell dumpsys gfxinfo，会把应用打开后的统计数据在返回，但只有每一帧总的统计数据，不包括具体每一个阶段的耗时，同时会有掉帧的总数，掉帧的比率，帧耗时的90线，95线，99线等。
         */

        // Choreographer.getInstance().postFrameCallback
        /*

         */
    }
}
