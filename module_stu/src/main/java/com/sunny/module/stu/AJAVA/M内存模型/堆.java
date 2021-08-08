package com.sunny.module.stu.AJAVA.M内存模型;

import com.sunny.module.stu.base.StuImpl;

public class 堆 extends StuImpl {

    @Override
    public void a_是什么() {
        // JVM 划分的一块内存区域
        // 线程共享
    }

    @Override
    public void c_功能() {
        // 存放对象实体
        /*
            实体对象，可以被【栈】中的局部变量引用
            可以被【方法区】的静态变量引用
            可以被其它实体对象的变量引用
         */
        // 当对象不再被引用时，则会被gc回收
        // GC工作区
    }
}
