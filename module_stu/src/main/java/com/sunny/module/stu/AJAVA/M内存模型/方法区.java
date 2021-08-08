package com.sunny.module.stu.AJAVA.M内存模型;

import com.sunny.module.stu.base.StuImpl;

public class 方法区 extends StuImpl {
    @Override
    public void a_是什么() {
        // JVM划分的一块内存区域
    }


    @Override
    public void c_功能() {
        // class类信息
        /*
            存储通过 classLoader 加载的类信息，通过类实例的 getClass 方法可获得数据的引用
         */

        // 字符串常量池
        /*
            1.8后，将其移出到堆中
         */
    }
}
