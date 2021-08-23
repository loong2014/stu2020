package com.sunny.module.stu.GLinux系统;

import com.sunny.module.stu.base.StuImpl;

/**
 * http://gityuan.com/2015/10/30/kernel-memory/
 */
public class Stu_内存管理 extends StuImpl {

    @Override
    public void a_是什么() {
        // 内核空间和用户空间
    }

    @Override
    public void s_数据结构() {

        // 页(page)是内核的内存管理基本单位。
        stu_页();

        // 内核把页划分在不同的区(zone)
        stu_区();

        // 内存描述符由mm_struct结构体表示
        stu_内存描述符();

        // 虚拟内存区域由vm_area_struct结构体描述， 指定地址空间内连续区间的一个独立内存范围。
        // 每个VMA代表不同类型的内存区域。
        stu_虚拟内存区域_VMA();

        // Linux使用三级页表完成地址转换
        stu_页表();
    }

    private void stu_页() {
        // 页(page)是内核的内存管理基本单位。
        /*
        ==> linux/mm_types.h
            struct page {
                   page_flags_t flags;  页标志符
                   atomic_t _count;    页引用计数
                   atomic_t _mapcount;     页映射计数
                   unsigned long private;    私有数据指针
                   struct address_space *mapping;    该页所在地址空间描述结构指针，用于内容为文件的页帧
                   pgoff_t index;               该页描述结构在地址空间radix树page_tree中的对象索引号即页号
                   struct list_head lru;        最近最久未使用struct slab结构指针链表头变量
                   void *virtual;               页虚拟地址
            };

            flags：页标志包含是不是脏的，是否被锁定等等，每一位单独表示一种状态，可同时表示出32种不同状态，定义在<linux/page-flags.h>
            _count：计数值为-1表示未被使用。
            virtual：页在虚拟内存中的地址，对于不能永久映射到内核空间的内存(比如高端内存)，该值为NULL；需要事必须动态映射这些内存。
         */
    }

    private void stu_区() {

        // 内核把页划分在不同的区(zone)
        /*
            总共3个区，具体如下：

            区	            描述	            物理内存（MB）
            ZONE_DMA	    DMA使用的页	    <16
            ZONE_NORMAL	    可正常寻址的页	    16 ~896
            ZONE_HIGHMEM	动态映射的页	    >896

            执行DMA操作的内存必须从ZONE_DMA区分配
            一般内存，既可从ZONE_DMA，也可从ZONE_NORMAL分配，但不能同时从两个区分配；
         */
    }

    private void stu_内存描述符() {

        // 内存描述符由mm_struct结构体表示
        /*
        ==> linux/sched.h
            struct mm_struct
            {
                struct vm_area_struct *mmap;
                rb_root_t mm_rb;
                ...
                atomic_t mm_users;
                atomic_t mm_count;

                struct list_head mmlist;
                ...
            };

            mm_users：代表正在使用该地址的进程数目，当该值为0时mm_count也变为0；
            mm_count: 代表mm_struct的主引用计数，当该值为0说明没有任何指向该mm_struct结构体的引用，结构体会被撤销。
            mmap和mm_rb：描述的对象都是相同的
                mmap以链表形式存放， 利于高效地遍历所有元素
                mm_rb以红黑树形式存放，适合搜索指定元素
            mmlist：所有的mm_struct结构体都通过mmlist连接在一个双向链表中，该链表的首元素是init_mm内存描述符，它代表init进程的地址空间。
         */
    }

    private void stu_虚拟内存区域_VMA() {
        // 虚拟内存区域由vm_area_struct结构体描述， 指定地址空间内连续区间的一个独立内存范围。 每个VMA代表不同类型的内存区域。
        /*
        ==> linux/mm_types.h
            struct vm_area_struct {
                struct mm_struct * vm_mm;  //内存描述符
                unsigned long  vm_start;   //区域的首地址
                unsigned long vm_end;      //区域的尾地址
                struct vm_area_struct * vm_next; //VMA链表
                pgrot t_vm_page_prot;   //访问控制权限
                unsigned long vm_flags;   //保护标志位和属性标志位
                struct rb_node_ vm_rb;   //VMA的红黑树结构
                ...
                struct vm_operations_struct * vm_ops; //相关的操作表
                struct file * vm_file; //指向被映射的文件的指针
                void * vm_private_data; //设备驱动私有数据，与内存管理无关。
            }

            每个内存描述符对应于进程地址空间的唯一区间，vm_end - vm_start便是内存区间的长度。
         */

        // VMA操作
        /*
        struct vm_operations_struct {
            void (*open) (struct vm_area_struct * area);
            void (*close) (struct vm_area_struct * area);
            struct page * (*nopage)(struct vm_area_struct *area, unsigned long address, int write_access);
            ...
        }
         */

        // 查看进程内存空间
        /*
        cat /proc/<pid>/maps
        每行数据格式： 开始-结束 访问权限 偏移 主设备号：次设备号 i节点 文件
            设备表示为00：00, 索引节点标示页为0，这个区域就是零页（所有数据全为零）
            数据段和bss具有可读、可写但不可执行权限；而堆栈可读、可写、甚至可执行

        也可通过工具pmap
        pmap <pid>
         */
    }

    private void stu_页表() {
        // Linux使用三级页表完成地址转换。
        /*
        应用程序操作的对象时映射到物理内存之上的虚拟内存，而处理器直接操作的是物理内存。
        故应用程序访问一个虚拟地址时，需要将虚拟地址转换为物理地址，然后处理器才能解析地址访问请求，
        这个转换工作通过查询页表完成。

        顶级页表：页全局目录(PGD)，指向二级页目录；
        二级页表：中间页目录(PMD)，指向PTE中的表项；
        最后一级：页表(PTE)，指向物理页面。

        多数体系结构，搜索页表工作由硬件完成。每个进程都有自己的页表(线程会共享页表)。
        为了加快搜索，实现了翻译后缓冲器(TLB)，作为将虚拟地址映射到物理地址的硬件缓存。
        还有写时拷贝方式共享页表，当fork()时，父子进程共享页表，只有当子进程或父进程试图修改特定页表项时，
            内核才创建该页表项的新拷贝，之后父子进程不再共享该页表项。
        可见，利用共享页表可以消除fork()操作中页表拷贝所带来的消耗。
         */
    }

}
