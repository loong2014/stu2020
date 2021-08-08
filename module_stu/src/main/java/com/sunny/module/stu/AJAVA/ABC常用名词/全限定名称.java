package com.sunny.module.stu.AJAVA.ABC常用名词;

import com.sunny.module.stu.base.StuImpl;

public class 全限定名称 extends StuImpl {

    @Override
    public void a_是什么() {
        // 全限定名称（Fully Qualified Name）
        /*
            全限定名是在整个JVM中的绝对名称，可以表示Class文件结构中的类或接口的名称。
            都通过全限定形式（Fully Qualified Form）来表示，这被称作它们的"二进制名称"（JLS §13.1）；
            但用来分隔各个标识符的符号不在是ASCII 字符点号（'.'），而是被 ASCII 字符斜杠（'/'）所代替。

            如，类 Thread 的正常的二进制名是"java.lang.Thread"，在 Class 文件面，
            对该类的引用是通过来一个代表字符串"java/lang/Thread"的CONSTANT_Utf8_info 结构来实现的。
         */
    }
}
