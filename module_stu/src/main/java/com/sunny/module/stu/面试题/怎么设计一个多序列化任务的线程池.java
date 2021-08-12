package com.sunny.module.stu.面试题;

import android.view.View;

import com.sunny.module.stu.base.StuImpl;

/**
 * https://blog.csdn.net/weixin_40151613/article/details/81835974
 */
public class 怎么设计一个多序列化任务的线程池 extends StuImpl {
    @Override
    public void a_是什么() {
        // IO型和计算型的区别
        /*
            Q：序列化操作属于cpu密集型么？
            A：不属于，属于I/O密集型

            Q：怎么制定线程池，核心线程数？
            A：
                cpu密集型：少点，cpu个数+1
                I/O密集型：多点，cpu个数*2
         */
    }
}
