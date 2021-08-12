package com.sunny.module.stu.面试题;

import com.sunny.module.stu.base.StuImpl;

import java.util.HashMap;

/**
 * https://blog.csdn.net/shumoyin/article/details/80243419
 */
public class HashMap的put过程 extends StuImpl {
    @Override
    public void a_是什么() {

        /*
            Q：介绍一下HashMap的put过程

            A：
                1、计算index
                2、新增/更新
                3、判断是否扩容
         */


        HashMap<String, String> map = new HashMap<>();
        map.put("aaa", "vvv");

    }

    @Override
    public void p_流程() {
        //

    }
}
