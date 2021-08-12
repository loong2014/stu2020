package com.sunny.module.stu.BAndroid.ABinder.概念;

import com.sunny.module.stu.base.StuImpl;

public class Stu_ioctl extends StuImpl {

    /**
     * linux 提供的命令，不同的cmd对于不同的操作
     * 在binder中，ioctl的cmd为BINDER_WRITE_READ，用于完成数据的接发
     */
    @Override
    public void 是什么() {
        /**
         * fd，cmd，param
         * 在binder中
         * cmd = 【BINDER_WRITE_READ】
         * param = binder_write_read 结构的引用
         */

        /*
        ioctl 是设备驱动程序中设备控制接口函数，一个字符设备驱动通常会实现设备打开、关闭、读、写等功能，
            在一些需要细分的情境下，如果需要扩展新的功能，通常以增设 ioctl() 命令的方式实现。

        2. 用户空间 ioctl
        int ioctl(int fd, int cmd, ...) ;
            fd：文件描述符
            cmd：交互协议，设备驱动将根据 cmd 执行对应操作
            ...：可变参数 arg，依赖 cmd 指定长度以及类型

        在实际应用中，ioctl 最常见的 errorno 值为 ENOTTY（error not a typewriter），顾名思义，
            即第一个参数 fd 指向的不是一个字符设备，不支持 ioctl 操作，
            这时候应该检查前面的 open 函数是否出错或者设备路径是否正确


        3. 驱动程序 ioctl
        long (*unlocked_ioctl) (struct file *, unsigned int, unsigned long);
        long (*compat_ioctl) (struct file *, unsigned int, unsigned long);

        在字符设备驱动开发中，一般情况下只要实现 unlocked_ioctl 函数即可，因为在 vfs 层的代码是直接调用 unlocked_ioctl 函数


        4. ioctl 用户与驱动之间的协议

        前文提到 ioctl 方法第二个参数 cmd 为用户与驱动的 “协议”，理论上可以为任意 int 型数据，
            可以为 0、1、2、3……，但是为了确保该 “协议” 的唯一性，ioctl 命令应该使用更科学严谨的方法赋值，
            在linux中，提供了一种 ioctl 命令的统一格式，将 32 位 int 型数据划分为四个位段，如下图所示：

        dir（direction），ioctl 命令访问模式（数据传输方向），占据 2 bit，
            可以为 _IOC_NONE、_IOC_READ、_IOC_WRITE、_IOC_READ | _IOC_WRITE，
            分别指示了四种访问模式：无数据、读数据、写数据、读写数据；

        type（device type），设备类型，占据 8 bit，在一些文献中翻译为 “幻数” 或者 “魔数”，
            可以为任意 char 型字符，例如‘a’、’b’、’c’ 等等，其主要作用是使 ioctl 命令有唯一的设备标识；

        nr（number），命令编号/序数，占据 8 bit，可以为任意 unsigned char 型数据，
            取值范围 0~255，如果定义了多个 ioctl 命令，通常从 0 开始编号递增；

        size，涉及到 ioctl 函数 第三个参数 arg ，占据 13bit 或者 14bit（体系相关，arm 架构一般为 14 位），
            指定了 arg 的数据类型及长度，如果在驱动的 ioctl 实现中不检查，通常可以忽略该参数；

         */
    }


}
