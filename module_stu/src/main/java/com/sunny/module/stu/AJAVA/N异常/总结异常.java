package com.sunny.module.stu.AJAVA.N异常;

import com.sunny.module.stu.base.StuImpl;

public class 总结异常 extends StuImpl {
    @Override
    public void a_是什么() {
        // 所有异常的父类
        Throwable throwable;

        // Throwable 的子类
        Error error; // 错误
        /*
            是程序中无法处理的错误，表示运行应用程序中出现了严重的错误
            此类错误一般表示代码运行时JVM出现问题。
            Virtual MachineError（虚拟机运行错误）、
            NoClassDefFoundError（类定义错误）等。
            比如说当jvm耗完可用内存时，将出现OutOfMemoryError。
            此类错误发生时，JVM将终止线程。非代码性错误。因此，当此类错误发生时，应用不应该去处理此类错误。
         */
        Exception exception; // 异常


        // Exception 的子类
        RuntimeException runtimeException; // 运行时异常(不受检异常)
        /*
            RuntimeException类极其子类表示JVM在运行期间可能出现的错误。
            编译器不会检查此类异常，并且不要求处理异常，比如
            用空值对象的引用（NullPointerException）、
            数组下标越界（ArrayIndexOutBoundException）。
            此类异常属于不可查异常，一般是由程序逻辑错误引起的，在程序中可以选择捕获处理，也可以不处理。
         */

        // 非运行时异常(受检异常)
        /*
            Exception中除RuntimeException极其子类之外的异常。
            编译器会检查此类异常，如果程序中出现此类异常，比如
            说IOException，必须对该异常进行处理，
            要么使用try-catch捕获，要么使用throws语句抛出，否则编译不通过。
         */
    }

    private int 异常处理(int num) throws Exception {

        // 抛出异常
        if (num < 10) {
            throw new Exception("num mast >= 10");
        }

        int result;
        // 捕获异常
        try {
            result = num / 0;
            // 没有直接返回
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常
        } finally {
            result = -1;
            // 无论是否发生异常，是否执行了 return，finally 代码块都会执行
        }
        return result;
    }
}
