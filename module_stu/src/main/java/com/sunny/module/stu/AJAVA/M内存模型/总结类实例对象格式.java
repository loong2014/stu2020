package com.sunny.module.stu.AJAVA.M内存模型;

import com.sunny.module.stu.base.StuImpl;


public class 总结类实例对象格式 extends StuImpl {

    @Override
    public void s_数据结构() {
        对象头();
        实例数据();
        对齐填充区域();
    }

    private void 对象头() {
        //普通对象 64 bits
        /*
        32：【MarkWord】 // 对象的运行时数据
        32：class pointer // 类型指针、指向方法区的class对象(类元数据)
        32：array length // 当对象是数组是存在，表示数据长度
         */


        stuMarkWord();
    }

    /*
    https://blog.csdn.net/zwjyyy1203/article/details/106217887
     */
    private void stu_ObjectMonitor(){

        // 通常所说的对象的内置锁，是对象头Mark Word中的重量级锁指针指向的monitor对象，
        // 该对象是在HotSpot底层C++语言编写的(openjdk里面看)，简单看一下代码：
        /*
//结构体如下
ObjectMonitor::ObjectMonitor() {
  _header       = NULL;
  _count       = 0;
  _waiters      = 0,
  _recursions   = 0;       //线程的重入次数
  _object       = NULL;
  _owner        = NULL;    //标识拥有该monitor的线程
  _WaitSet      = NULL;    //等待线程组成的双向循环链表，_WaitSet是第一个节点
  _WaitSetLock  = 0 ;
  _Responsible  = NULL ;
  _succ         = NULL ;
  _cxq          = NULL ;    //多线程竞争锁进入时的单向链表
  FreeNext      = NULL ;
  _EntryList    = NULL ;    //_owner从该双向循环链表中唤���线程结点，_EntryList是第一个节点
  _SpinFreq     = 0 ;
  _SpinClock    = 0 ;
  OwnerIsThread = 0 ;
}
         */
    }
    private void stuMarkWord() {

/*
|-------------------------------------------------------|--------------------|
|                  Mark Word (32 bits)                  |       State        |
|-------------------------------------------------------|--------------------|
| identity_hashcode:25 | age:4 | biased_lock:1 | lock:2 |       Normal       |
    identity_hashcode：25位的对象标识Hash码，采用延迟加载技术。调用方法System.identityHashCode()计算，
    并会将结果写到该对象头中。当对象被锁定时，该值会移动到管程Monitor中。

    biased_lock：对象是否启用偏向锁标记，只占1个二进制位。为1时表示对象启用偏向锁，为0时表示对象没有偏向锁。

    age：4位的Java对象年龄。在GC中，如果对象在Survivor区复制一次，年龄增加1。当对象达到设定的阈值时，将会晋升到老年代。
    默认情况下，并行GC的年龄阈值为15，并发GC的年龄阈值为6。由于age只有4位，所以最大值为15，
    这就是-XX:MaxTenuringThreshold选项最大值为15的原因。

|-------------------------------------------------------|--------------------|
|  thread:23 | epoch:2 | age:4 | biased_lock:1 | lock:2 |       Biased       |
    thread：持有偏向锁的线程ID
    epoch：偏向时间戳。

|-------------------------------------------------------|--------------------|
|               ptr_to_lock_record:30          | lock:2 | Lightweight Locked |
    ptr_to_lock_record：指向栈中锁记录的指针。

|-------------------------------------------------------|--------------------|
|               ptr_to_heavyweight_monitor:30  | lock:2 | Heavyweight Locked |
    ptr_to_heavyweight_monitor：指向管程Monitor的指针。

|-------------------------------------------------------|--------------------|
|                                              | lock:2 |    Marked for GC   |
|-------------------------------------------------------|--------------------|
lock:2位的锁状态标记位，由于希望用尽可能少的二进制位表示尽可能多的信息，所以设置了lock标记。该标记的值不同，整个mark word表示的含义不同。
         biased_lock	lock	状态
            0	        01	    无锁
            1	        01	    偏向锁
            0	        00	    轻量级锁
            0	        10	    重量级锁
            0	        11	    GC标记
 */


/*
|------------------------------------------------------------------------------|--------------------|
|                                  Mark Word (64 bits)                         |       State        |
|------------------------------------------------------------------------------|--------------------|
| unused:25 | identity_hashcode:31 | unused:1 | age:4 | biased_lock:1 | lock:2 |       Normal       |
|------------------------------------------------------------------------------|--------------------|
| thread:54 |       epoch:2        | unused:1 | age:4 | biased_lock:1 | lock:2 |       Biased       |
|------------------------------------------------------------------------------|--------------------|
|                       ptr_to_lock_record:62                         | lock:2 | Lightweight Locked |
|------------------------------------------------------------------------------|--------------------|
|                     ptr_to_heavyweight_monitor:62                   | lock:2 | Heavyweight Locked |
|------------------------------------------------------------------------------|--------------------|
|                                                                     | lock:2 |    Marked for GC   |
|------------------------------------------------------------------------------|--------------------|
 */
        /*
        identity_hashcode：25位的对象标识Hash码，采用延迟加载技术。调用方法System.identityHashCode()计算，并会将结果写到该对象头中。当对象被锁定时，该值会移动到管程Monitor中。
        thread：持有偏向锁的线程ID。


         */

    }

    private void 实例数据() {
        /*
        对象真正有效的信息，即类的字段信息
        空间分配顺序策略：long/double,int,short/char,byte/boolean,oop
         */
    }

    private void 对齐填充区域() {
        /*
        占位符，对象的大小必须是【8字节】的整数倍
         */
    }
}
