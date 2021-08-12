package com.sunny.module.stu.面试题;

import com.sunny.module.stu.base.StuImpl;

/**
 * https://blog.csdn.net/rfgreeee/article/details/79088510
 */
public class getWidth和getMeasuredWidth的区别 extends StuImpl {

    @Override
    public void a_是什么() {
        /*
            Q：getWidth 和 getMeasuredWidth 的区别

            A：
                getMeasuredWidth()获取的是view测量后的大小（onMeasure），
                getWidth（）获取的是这个view最终显示（区域）的大小(父布局的onLayout有关)
         */
    }
}
