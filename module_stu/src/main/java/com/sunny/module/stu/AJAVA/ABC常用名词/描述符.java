package com.sunny.module.stu.AJAVA.ABC常用名词;

import com.sunny.module.stu.base.StuImpl;

public class 描述符 extends StuImpl {

    /*
    https://blog.csdn.net/tjiyu/article/details/53870153
     */
    @Override
    public void a_是什么() {
        // 描述符（Descriptor）
        /*
            1、字段描述符
            2、方法描述符
         */

        /*
            描述符是一个描述字段或方法的类型的字符串。

        A）、字段描述符（Field Descriptor）：
            是一个表示类、实例或局部实例变量的语法符号；

        B）、方法描述符（Method Descriptor）
            描述一个方法所需的参数和返回值信息，即包括参数描述符（ParameterDescriptor）和返回描值述符(ReturnDescriptor)；

            一个方法无论是静态方法还是实例方法，它的方法描述符都是相同的；
            方法额外传递参数this，不是由法描述符来表达的；而是由 Java 虚拟机实现在调用实例方法所使用的指令中实现的隐式传递；

        C）、描述符中基本类型表示字符如下：
            描述int实例变量的描述符是"I"；
            java.lang.Object实例描述符是"Ljava/lang/Object;"；
            double的三维数组"double d[][][];"的描述符为"[[[D"；
            Object mymethod(int i, double d, Thread t)方法描述符是"(IDLjava/lang/Thread;)Ljava/lang/Object";
         */
    }

    private void stu描述符中基本类型() {

    }
}
