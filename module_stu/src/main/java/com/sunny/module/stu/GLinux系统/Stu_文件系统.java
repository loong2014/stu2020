package com.sunny.module.stu.GLinux系统;

import com.sunny.module.stu.base.StuImpl;

public class Stu_文件系统 extends StuImpl {

    @Override
    public void a_是什么() {

        // 万物皆文件
        /*
            在unix里，“一切皆为文件”，设备也会被视为文件。
                包括 网络连接套接字、管道
         */

        /*
            扇区(Sector)：硬盘的最小【存储】单位，每个扇区储存512字节（相当于0.5KB）

            块(block)：操作系统【读取】硬盘的最小单位，最常见的是4KB（既连续八个sector组成一个block）
                文件数据存储在【块】中

            索引节点(inode)：存储文件【元信息】，包括文件数据block的位置

            inode号码：每个inode都有一个号码，操作系统用inode号码来识别不同的文件。

            目录(directory)：目录项列表
                目录项：文件的文件名，以及该文件名对应的inode号码

            文件描述符(fd)：文件描述符是一个抽象的用于访问文件或者其他输入输出资源一个指示符。

            inode table：存储inode信息

         */

        // 文件系统如何取文件
        /*
        1、根据文件名，通过Directory里的对应关系，找到文件对应的Inode number
        2、再根据Inode number读取到文件的Inode table
        3、再根据Inode table中的Pointer读取到相应的Blocks
         */


        // mount
        /*
        将文件系统和存储设备联系起来的方法：挂载(mounting)。
        mount命令通常被用于将文件系统和当前文件目录级别联系起来。
            在mount过程中需要提供文件系统类型、文件系统、挂载点。

            第一步：基于当前文件系统中的文件创建一个文件系统file256mb.img
                    (file256mb.img对于当前文件系统来说是一个文件)。
                dd if=/dev/zero of=file256mb.img bs=4k count=65536

            第二步：使用mke2fs在file265mb.img上创建文件系统
                    (从file256mb.img自身角度来说，他是一个文件系统了)
                mke2fs -t ext4 -c file265mb.img

            第三步：创建一个挂载点
                    (从当前系统角度来说，他就是一个目录，一个文件；
                    但是从即将要挂在文件系统的角度来说，他就是一个存储设备)
                mkdir /path/to/mount/point

            第四步：将文件系统挂载到当前文件级别
                    (将文件系统和存储设备关联起来)
                mount -t ext4 file256mb.img /path/to/point
         */
    }

}
