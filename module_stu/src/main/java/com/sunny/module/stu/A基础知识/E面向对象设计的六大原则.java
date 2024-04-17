package com.sunny.module.stu.A基础知识;

import com.sunny.module.stu.base.StuImpl;

public class E面向对象设计的六大原则 extends StuImpl {

    @Override
    public void a_是什么() {
        // SOLID + 迪米特
    }

    @Override
    public void s_数据结构() {
        /*
        面向对象设计的六大原则是指在进行面向对象设计和编程时，开发者应遵循的一些基本规则，以增强程序的可维护性、可复用性、可扩展性等。这六大原则如下：

         */
        // 单一职责原则（Single Responsibility Principle, SRP）
        /*
            只做一件事，实现一个功能
         */

        // 开放封闭原则（Open-Closed Principle, OCP）
        /*
            对扩展开发，对修改关闭。即有新需求时，可以扩展新方法，不能修改已有方法
         */

        // 里氏替换原则（Liskov Substitution Principle, LSP）
        /*
            调用父类的地方，可以调用子类，即子类不应该修改父类的非抽象方法
         */

        // 接口隔离原则（Interface Segregation Principle, ISP）
        /*
            接口功能单一
         */

        // 依赖倒置原则（Dependency Inversion Principle, DIP）
        /*
            上层模块的实现，不依赖下层模块的具体实现。也就是面向接口编程
         */

        // 迪米特原则（Law of Demeter, LoD）
        /*
            最少知道原则，低耦合高内聚
            一个对象应当对其他对象有最少的了解，不过度依赖
         */

    }
}
