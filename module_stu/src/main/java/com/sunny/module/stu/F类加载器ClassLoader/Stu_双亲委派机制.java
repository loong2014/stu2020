package com.sunny.module.stu.F类加载器ClassLoader;

import com.sunny.module.stu.base.StuImpl;

public class Stu_双亲委派机制 extends StuImpl {
    @Override
    public void a_是什么() {
        /*
            当一个ClassLoader去加载一个类的时候，它会去判断该类是否已经加载，如果没有，它不会马上去加载，
            而是委托给父加载器进行查找，这样递归一直找到最上层的ClassLoader类，
            如果找到了，就直接返回这个类所对应的Class对象，如果都没有加载过，
            就从顶层的ClassLoader去开始依次向下查找，每个加载器会从自己规定的位置去查找这个类，
            如果没有，最后再由请求发起者去加载该类

            简单说，就是第一次查找的时候，是从下到上依次从缓存中查找之前有没有加载过，如果有就返回，
            如果都没有，就从上到下从自己制定的位置去查找这个类，最后在交给发起者去加载该类。
         */
    }

    @Override
    public void b_作用() {
        //双亲委派机制的好处
        /*
            1：避免重复加载，如果已经加载过一次Class，就不需要再次加载，而是先从缓存中直接读取。

            2：更加安全，因为虚拟机认为只有两个类名一致并且被同一个类加载器加载的类才是同一个类，所以这种机制保证了系统定义的类不会被替代。
         */
    }

}
