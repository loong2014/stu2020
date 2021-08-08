package com.sunny.module.stu.AJAVA.M内存模型;

import com.sunny.module.stu.base.StuImpl;

public class 栈帧 extends StuImpl {

    @Override
    public void a_是什么() {
        // 方法调用和方法执行的数据结构
    }

    @Override
    public void s_数据结构() {
        // 局部变量表
        /*
            读取方法表中 Code_attribute 的 max_locals 字段，生成变量表
            变量表的存储单位是 变量槽(slot)
         */

        // 操作数栈
        /*
            读取方法表中 Code_attribute 的 max_stack 字段，生成操作数栈


         */

        // 动态链接
        /*
            指向所属方法的引用
         */

        // 方法返回地址
        /*
            恢复上层方法的局部变量表和操作数栈
            把返回值压入调用者栈帧的操作数栈
            调整pc计数器的值，指向方法调用指令，后面的指令
         */
    }
}
