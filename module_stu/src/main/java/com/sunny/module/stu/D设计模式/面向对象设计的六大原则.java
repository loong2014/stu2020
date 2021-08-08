package com.sunny.module.stu.D设计模式;

import com.sunny.module.stu.base.StuImpl;

public class 面向对象设计的六大原则 extends StuImpl {

    @Override
    public void a_是什么() {
        // SOLID + 迪米特
    }

    @Override
    public void s_数据结构() {
        // 单一指责
        /*
            只做一件事，实现一个功能
         */

        // 开闭原则
        /*
            对扩展开发，对修改关闭。即有新需求时，可以扩展新方法，不能修改已有方法
         */

        // 里氏替换
        /*
            调用父类的地方，可以调用子类，即子类不应该修改父类的非抽象方法
         */

        // 接口隔离
        /*
            接口功能单一
         */

        // 依赖倒置
        /*
            上层模块的实现，不依赖下层模块的具体实现。也就是面向接口编程
         */

        // 迪米特
        /*
            低耦合高内聚
         */

    }
}
