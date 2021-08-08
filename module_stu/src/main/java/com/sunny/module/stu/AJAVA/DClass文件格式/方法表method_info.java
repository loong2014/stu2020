package com.sunny.module.stu.AJAVA.DClass文件格式;

import com.sunny.module.stu.base.StuImpl;

public class 方法表method_info extends StuImpl {


    @Override
    public void s_数据结构() {
        /*
method_info{
    u2                  access_flags // 用于定义当前方法的访问权限和基本属性的掩码标志；
    u2                  name_index   // 指向CONSTANT_Utf8_info类型常量的索引，表示当前方法的非全限定名（简单名称）；
    u2                  descriptor_index // 指向CONSTANT_Utf8_info类型常量的索引，表示当前方法的描述符；
    u2                  attributes_count // 表示当前方法的附加属性的数量
    attribute_info      attributes[attributes_count]
                            表示当前方法的一些属性信息：
                            1、如重要的"Code"属性，表示方法编译后的字节码指令
                            2、有泛型参数存在Signature属性；
}
         */
    }
}
