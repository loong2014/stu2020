package com.sunny.module.stu.AJAVA.DClass文件格式.属性表;

import com.sunny.module.stu.base.StuImpl;

public class Code_attribute extends StuImpl {

    @Override
    public void a_是什么() {

        /*
               Java方法体中的代码经过javac编译处理，生成字节码的指令信息；

       出现在方法表的属性集合中，也可能没有（如方法被声明为native或者abstract类型
         */
    }

    @Override
    public void s_数据结构() {
/*
    Code_attribute{
        u2          attribute_name_index // 指向CONSTANT_Utf8_info类型常量的索引，常量中固定字符串"Code"
        u4          attribute_length // 表示当前属性的长度，不包括开始的 6 个字节
        u2          max_stack // 给出了当前方法的【操作数栈】的最大深度（在运行执行的任何时间点都不超过），
                            JVM运行时根据这个值分配栈帧（Stack Frame）中的操作栈深度
        u2          max_locals // 给出了分配在当前方法引用的局部变量表中的局部变量个数，包括调用此方法时用于传递参数的局部变量
                            其实是局部变量的存储空间大小，单位是Slot（JVM为局部变量分配内存的最小单位）
                            long和double类型的局部变量的占两个Slot，其它类型的局部变量的只占一个.
                            执行超出局部变量作用域后，变量占的Slot空间可以被其他变量重用
        u4          code_length // code_length项给出了当前方法的 code[]数组的字节数
                            code_length虽然是u4类型，但0<code_length<65536，即code[]数组不能为空，也不能超过65536，
                            所以程序中方法体不能写得过长
        u1          code[code_length] // 【存储实现当前方法的JVM字节码指令】
                             一个code是u1类型，最大可以表示256个指令；
                             目前JVM规范已经定义了其中约200条编译值对应的指令含义
        u2          exception_table_length  //
attribute_info      exception_table[exception_table_length] //
        u2          attributes_count  //
attribute_info      exception_table[attributes_count] // 一个Code属性可以有任意数量的可选属性与之关联，只能是：
                                    LineNumberTable（程序行号信息）、
                                    LocalVariableTable（栈上变量信息）、
                                    LocalVariableTypeTable
                                    StackMapTable属性
    }
 */

    }

    private void stu_exception_table() {
        /*
    exception_table {
        u2      start_pc    //
        u2      end_pc      // 两项的值表明了异常处理器在 code[]数组中的有效范围
        u2      handler_pc  // 表示一个异常处理器的起点，它的值必须同时是一个对当前 code[]数组中某一指令的操作码的有效索引；
        u2      catch_type  // 指向CONSTANT_Class_info类型常量的索引，表示要捕获异常的类型
                            为0表示捕获所有类型的异常，可以用来实现finally语句
    }

         */
    }
}
