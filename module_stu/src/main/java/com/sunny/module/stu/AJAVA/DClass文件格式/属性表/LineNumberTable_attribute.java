package com.sunny.module.stu.AJAVA.DClass文件格式.属性表;

import com.sunny.module.stu.base.StuImpl;

public class LineNumberTable_attribute extends StuImpl {

    @Override
    public void a_是什么() {
        /*
        用于描述Java源码行号与字节码偏移量之间的对应关系；

       位于Code属性的属性表集合中，可以按照任意顺序出现；

       不是运行必须的；

       默认不生成到Class文件中，可以javac -g:none取消或-g:lines要求生成；

       不生成的话影响调试，如抛出异常时堆栈中不显示出错行号，也无法设置断点；
         */
    }

    @Override
    public void s_数据结构() {
/*
    LineNumberTable_attribute{
        u2          attribute_name_index    // 指向CONSTANT_Utf8_info类型常量的索引，表示字符串"LineNumberTable"
        u4          attribute_length
        u2          line_number_table_length  //
line_number_table      line_number_table[line_number_table_length] //
    }
 */

    }

    private void line_number_table() {
        /*
    line_number_table {
        u2      start_pc    // 必须是code[]数组的一个索引，code[]数组在该索引处的字符表示源文件中新的行的起点
        u2      line_number      // 必须与源文件的行号相匹配
    }
         */
    }
}
