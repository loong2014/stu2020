package com.sunny.module.stu.AJAVA.O注解;

import com.sunny.module.stu.base.StuImpl;

/**
 * https://blog.csdn.net/wuyuxing24/article/details/81139846
 */
public class Stu元注解 extends StuImpl {

    @Override
    public void a_是什么() {
        /*
        java.lang.annotation提供了四种元注解
        元注解	说明
        @Target	    表明我们注解可以出现的地方。是一个ElementType枚举
        @Retention	这个注解的的存活时间
        @Document	表明注解可以被javadoc此类的工具文档化
        @Inherited	是否允许子类继承该注解，默认为false
         */

    }

    private void stuTarget() {
        /*
        @Target-ElementType类型	说明
        ElementType.TYPE	接口、类、枚举、注解
        ElementType.FIELD	字段、枚举的常量
        ElementType.METHOD	方法
        ElementType.PARAMETER	方法参数
        ElementType.CONSTRUCTOR	构造函数
        ElementType.LOCAL_VARIABLE	局部变量
        ElementType.ANNOTATION_TYPE	注解
        ElementType.PACKAGE	包
         */
    }

    private void stuRetention() {
        /*
        @Retention-RetentionPolicy类型	说明
        RetentionPolicy.SOURCE	注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃
        RetentionPolicy.CLASS	注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期
        RetentionPolicy.RUNTIME	注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
         */

    }

    private void stuDocument() {
        //        @Document表明我们标记的注解可以被javadoc此类的工具文档化。

    }

    private void stuInherited() {
//       @Inherited表明我们标记的注解是被继承的。比如，如果一个父类使用了@Inherited修饰的注解，则允许子类继承该父类的注解。
    }
}
