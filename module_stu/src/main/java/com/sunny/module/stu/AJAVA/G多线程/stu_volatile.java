package com.sunny.module.stu.AJAVA.G多线程;

import com.sunny.module.stu.base.StuImpl;

public class stu_volatile extends StuImpl {
    volatile int age;

    @Override
    public void c_功能() {

        // 可见性
        /*
            数据发生变化后，会通知所有引用重新获取值
         */
        // 有序性
        /*
            内存屏障
         */
    }
}
