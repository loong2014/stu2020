package com.sunny.module.stu.AJAVA.C类加载;

import com.sunny.module.stu.base.StuImpl;

public class 总结类生命周期 extends StuImpl {

    @Override
    public void a_是什么() {
        // 加载
        /*
        通过 ClassLoader 将类信息加载到JVM的【方法区】，并将其转换成一个与目标类型对应的class对象
        这个class对象就会作为方法区中该类的各种数据的访问入口
         */

        // 链接
        // 1、验证
            /*
                格式验证
                语义验证
                操作验证
             */

        // 2、准备
            /*
                给静态变量分配空间
                给final类型的静态变量赋值
             */

        // 3、解析
            /*
                将【常量池】中的【符号引用】替换为【直接引用】

                【符号引用】：
                    符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时能够无歧义的定位到目标即可。
                    例如，在Class文件中它以CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info等类型的常量出现。
                    符号引用与虚拟机的内存布局无关，引用的目标并不一定加载到内存中。在Java中，一个java类将会编译成一个class文件。
                    在编译时，java类并不知道所引用的类的实际地址，因此只能使用符号引用来代替。
                    比如org.simple.People类引用了org.simple.Language类，在编译时People类并不知道Language类的实际内存地址，
                    因此只能使用符号org.simple.Language（假设是这个，当然实际中是由类似于CONSTANT_Class_info的常量来表示的）来表示Language类的地址。
                    各种虚拟机实现的内存布局可能有所不同，但是它们能接受的符号引用都是一致的，因为符号引用的字面量形式明确定义在Java虚拟机规范的Class文件格式中。

                【直接引用】：
                    （1）直接指向目标的指针（比如，指向“类型”【Class对象】、类变量、类方法的直接引用可能是指向方法区的指针）
                    （2）相对偏移量（比如，指向实例变量、实例方法的直接引用都是偏移量）
                    （3）一个能间接定位到目标的句柄
             */

        // 初始化
        /*
            给静态变量赋值，执行静态代码块
         */

        // 使用
        // 主动引用
                /*
                1、new
                2、调用 static 变量/方法
                3、反射
                4、初始化子类
                 */
        // 被动引用
                /*
                1、调用父类 static 变量/方法，不会引起子类初始化
                2、调用static final 变量
                3、定义类数组
                 */


        // 卸载
        // GC
    }
}
