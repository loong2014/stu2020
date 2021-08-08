package com.sunny.module.stu.AJAVA.DClass文件格式;

import com.sunny.module.stu.base.StuImpl;

public class 总结Class字节码格式 extends StuImpl {


    /**
     * https://www.cnblogs.com/rollenholt/articles/2176758.html
     */
    @Override
    public void s_数据结构() {
/*
    type            |   descriptor | remark
    --------------------------------------------
    u4              |   magic   |   0xCAFEBABE  // 魔数
    u2              |   minor_version   // 次版本号   //major_version.minor_version合在一起形成当前.class文件的版本号
    u2              |   major_version   // 主版本号
    u2              |   constant_pool_count // class 常量池大小
    cp_info         |   constant_pool[constant_pool_count – 1]  |   index 0 is invalid
    u2              |   access_flags
    u2              |   this_class
            // 指向constant pool的索引值，该值必须是CONSTANT_Class_info类型，指定当前字节码定义的类或接口。
    u2              |   super_class
            // 指向constant pool的索引值，该值必须是CONSTANT_Class_info类型，指定当前字节码定义的类或接口的直接父类。
            只有Object类才没有直接父类，此时该索引值为0。并且父类不能是final类型。接口的父类都是Object类。
    u2              |   interfaces_count
    u2              |   interfaces[interfaces_count]
            // interfaces数组记录所有当前类或接口直接实现的接口。
            interfaces数组中的每项值都是一个指向constant pool的索引值，这些值必须是CONSTANT_Class_info类型。
            数组中接口的顺序和源代码中接口定义的顺序相同。
    u2              |   fields_count
    field_info      |   fields[fields_count]
            // fields数组记录了类或接口中的所有字段，包括实例字段和静态字段，但不包含父类或父接口中定义的字段。
            fields数组中每项都是field_info类型值，它描述了字段的详细信息，如名称、描述符、字段中的attribute等。
    u2              |   methods_count
    method_info     |   methods[methods_count]
    u2              |   attributes_count
    attribute_info  |   attributes[attributes_count]
*/


    }
}

