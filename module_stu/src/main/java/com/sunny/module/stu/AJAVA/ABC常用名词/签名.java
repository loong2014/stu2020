package com.sunny.module.stu.AJAVA.ABC常用名词;

import com.sunny.module.stu.base.StuImpl;


/*
https://blog.csdn.net/tjiyu/article/details/53870153
 */
public class 签名 extends StuImpl {
    @Override
    public void a_是什么() {
        // 签名是JDK1.5引入泛型（类型变量或参数化类型）后的而出现的；
        // 签名需要（字段、方法和类中）有类型变量或参数化类型的时候才会出现。
        /*
            1、类签名
            2、字段类型签名
            3、方法签名
         */

        //签名（Signature）
        /*
            签名是用于描述字段、方法和类型定义中的泛型信息的字符串，这应该是JDK1.5引入泛型（类型变量或参数化类型）后的而出现的。

       Java编译器必须为声明使用类型变量或参数化类型的任何类、接口、构造函数、方法或字段发出签名。
       类型变量或参数化类型在编译时经过类型擦除变为原始类型，所以它们都是不在Java虚拟机中使用的类型，
       而Java编译器需要这类信息来实现（或辅助实现）反射（reflection）和跟踪调试功能。

       HotSpot VM实现在加载和链接时，并不校验Class文件的签名内容，直到被反射方法调用时才会校验。

       在Java语言中，任何类、接口、初始化方法或成员的泛型签名，如果包含了类型变量（Type Variables）或参数化类型（Parameterized Types），
       则该字段在字段表集合中（field_info fields[fields_count]）对应的字段信息（field_info），
       或该方法在方法表集合（method_info methods[methods_count]）对应的方法信息（method_info），
       存在Signature属性会为它记录泛型签名信息，Signature属性存在指向CONSTANT_Utf8_info常量类型数据的索引，这样就可以找到相应的签名字符串。

（A）、类签名（Class Signature）

       作用是把Class申明的类型信息编译成对应的签名信息；

       描述当前类可能包含的所有的（泛型类型的）形式类型参数，包括直接父类和父接口；

       由 ClassSignature 定义：



（B）、字段类型签名（Field Type Signature）

       作用是将字段、参数或局部变量的类型编译成对应的签名信息；

       由 JavaTypeSignature定义，包括基本类型和引用类型的签名：







（C）、方法签名（Method Signature）

       作用是将方法中所有的形式参数的类型编译成相应的签名信息（或将它们参数化）；

       由 MethodTypeSignature 定义：



       计算方法的特征签名在前文《Java前端编译：Java源代码编译成Class文件的过程》"2-2、填充符号表 第4点、计算方法的特征签名"时曾提到过；
         */
    }
}
