package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class Fmmap extends StuImpl {

    /*
    void* mmap(void* start,size_t length,int prot,int flags,int fd,off_t offset);
     */
    @Override
    public void 是什么() {
        // 内存映射
        /*
            将虚拟地址，映射到物理地址
         */

        // 每页大小4096字节，即4k
        // 物理页
        /*
            将物理内存分割为(PhysicalPage PP)大小的固定块
         */

        // 虚拟页
        /*
            将虚拟内存分割为(VirtualPage VP)大小的固定块
         */

        // mmap 用来在虚拟内存地址空间分配地址空间，创建和物理内存的映射关系
        // 简单说，就是管理 VP 到 PP，可以通过 VP 访问 PP


    }
}
