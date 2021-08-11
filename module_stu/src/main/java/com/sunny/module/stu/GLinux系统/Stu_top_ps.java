package com.sunny.module.stu.GLinux系统;

import com.sunny.module.stu.base.StuImpl;

public class Stu_top_ps extends StuImpl {
    @Override
    public void a_是什么() {
        // ps:  显示所有活动进程
        // top: 实时动态显示进程信息
        /*
            两个命令都是查看系统进程信息，但是
                ps命令是查看系统过去信息的一次性快照或者瞬时的信息，
                top查看系统进程动态信息，默认10秒钟更新一次，可以持续监视进程。
         */
    }

    @Override
    public void d_怎么用() {
        // top
        /*
            top -m 5    // 打印前五个
            top -m 5 -d 2 // 打印前五个，间隔2s
            top -m 5 -d 2 -n 10 // 打印前五个，间隔2s，打印10次

        //  每隔2s，打印10次，只打印进程名包含"xxx"的
            top -d 2 -n 10 | grep xxx

         */
        // ps
    }

    @Override
    public void s_数据结构() {
        // top
        /*
User 3%, System 2%, IOW 0%, IRQ 0%
User 45 + Nice 1 + Sys 35 + Idle 1122 + IOW 0 + IRQ 0 + SIRQ 10 = 1213

  PID PR CPU% S  #THR     VSS     RSS PCY UID      Name
 1857  0   1% S    56 1606496K  28608K  fg root     /applications/bin/tvos
 2892  1   0% S   189 1088780K  44392K  bg system   com.stv.reportlog
         */

        // ps
        /*
USER      PID   PPID  VSIZE  RSS   WCHAN              PC  NAME
root      1     0     1516   676   SyS_epoll_ 00004b2d90 S /init
root      2     0     0      0       kthreadd 0000000000 S kthreadd
root      3     2     0      0     smpboot_th 0000000000 S ksoftirqd/0
         */
    }
}
