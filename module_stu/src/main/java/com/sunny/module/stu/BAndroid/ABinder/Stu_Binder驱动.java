package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

/**
 * http://gityuan.com/2015/11/01/binder-driver/
 */
public class Stu_Binder驱动 extends StuImpl {

    @Override
    public void a_是什么() {
        /*
        Binder驱动是Android专用的，但底层的驱动架构与Linux驱动一样。
        binder驱动在以misc设备进行注册，作为虚拟字符设备，没有直接操作硬件，只是对设备内存的处理。
        主要是驱动设备的
            初始化(binder_init)，
            打开 (binder_open)，
            映射(binder_mmap)，
            数据操作(binder_ioctl)。

         */
    }

    private void stu_系统调用() {
        /*
        用户态的程序调用Kernel层驱动是需要陷入内核态，进行系统调用(syscall)，
        比如打开Binder驱动方法的调用链为： open-> __open() -> binder_open()。
            open()为用户空间的方法，
            __open()便是系统调用中相应的处理方法，通过查找，
            对应调用到内核binder驱动的binder_open()方法，
        至于其他的从用户态陷入内核态的流程也基本一致。


        简单说，当用户空间调用open()方法，最终会调用binder驱动的binder_open()方法；
        mmap()/ioctl()方法也是同理，在BInder系列的后续文章从用户态进入内核态，都依赖于系统调用过程。


         */
    }

    private void binder_init() {
        // 主要工作是为了注册misc设备
        /*
        static int __init binder_init(void)
        {
            int ret;
            //创建名为binder的工作队列
            binder_deferred_workqueue = create_singlethread_workqueue("binder");
            ...

            binder_debugfs_dir_entry_root = debugfs_create_dir("binder", NULL);
            if (binder_debugfs_dir_entry_root)
                binder_debugfs_dir_entry_proc = debugfs_create_dir("proc",
                                 binder_debugfs_dir_entry_root);

             // 注册misc设备【见小节2.1.1】
            ret = misc_register(&binder_miscdev);
            if (binder_debugfs_dir_entry_root) {
                ... //在debugfs文件系统中创建一系列的文件
            }
            return ret;
        }
         */
    }

    private void binder_open() {
        // 打开binder驱动设备
        /*
        static int binder_open(struct inode *nodp, struct file *filp)
        {
            struct binder_proc *proc; // binder进程 【见附录3.1】

            proc = kzalloc(sizeof(*proc), GFP_KERNEL); // 为binder_proc结构体在分配kernel内存空间
            if (proc == NULL)
                return -ENOMEM;
            get_task_struct(current);
            proc->tsk = current;   //将当前线程的task保存到binder进程的tsk
            INIT_LIST_HEAD(&proc->todo); //初始化todo列表
            init_waitqueue_head(&proc->wait); //初始化wait队列
            proc->default_priority = task_nice(current);  //将当前进程的nice值转换为进程优先级

            binder_lock(__func__);   //同步锁，因为binder支持多线程访问
            binder_stats_created(BINDER_STAT_PROC); //BINDER_PROC对象创建数加1
            hlist_add_head(&proc->proc_node, &binder_procs); //将proc_node节点添加到binder_procs为表头的队列
            proc->pid = current->group_leader->pid;
            INIT_LIST_HEAD(&proc->delivered_death); //初始化已分发的死亡通知列表
            filp->private_data = proc;       //file文件指针的private_data变量指向binder_proc数据
            binder_unlock(__func__); //释放同步锁

            return 0;
        }

        创建binder_proc对象，并把当前进程等信息保存到binder_proc对象，
            该对象管理IPC所需的各种信息并拥有其他结构体的根结构体；
        再把binder_proc对象保存到文件指针filp，以及把binder_proc加入到全局链表binder_procs。

        Binder驱动中通过static HLIST_HEAD(binder_procs);，创建了全局的哈希链表binder_procs，
            用于保存所有的binder_proc队列，每次新创建的binder_proc对象都会加入binder_procs链表中。


         */
    }

    private void binder_mmap() {
        // binder_mmap(文件描述符，用户虚拟内存空间)
        /*
        主要功能：
        首先在【内核虚拟地址空间】，申请一块与【用户虚拟内存】相同大小的内存；
        然后再申请1个page大小的【物理内存】，再将同一块【物理内存】分别映射到【内核虚拟地址空间】和【用户虚拟内存空间】，
        从而实现了【用户空间】的Buffer和【内核空间】的Buffer同步操作的功能。

        static int binder_mmap(struct file *filp, struct vm_area_struct *vma)
        {
            int ret;
            struct vm_struct *area; //内核虚拟空间
            struct binder_proc *proc = filp->private_data;
            const char *failure_string;
            struct binder_buffer *buffer;  //【见附录3.9】

            if (proc->tsk != current)
                return -EINVAL;

            if ((vma->vm_end - vma->vm_start) > SZ_4M)
                vma->vm_end = vma->vm_start + SZ_4M;  //保证映射内存大小不超过4M

            mutex_lock(&binder_mmap_lock);  //同步锁
            //采用IOREMAP方式，分配一个连续的内核虚拟空间，与进程虚拟空间大小一致
            area = get_vm_area(vma->vm_end - vma->vm_start, VM_IOREMAP);
            if (area == NULL) {
                ret = -ENOMEM;
                failure_string = "get_vm_area";
                goto err_get_vm_area_failed;
            }
            proc->buffer = area->addr; //指向内核虚拟空间的地址
            //地址偏移量 = 用户虚拟地址空间 - 内核虚拟地址空间
            proc->user_buffer_offset = vma->vm_start - (uintptr_t)proc->buffer;
            mutex_unlock(&binder_mmap_lock); //释放锁

            ...
            //分配物理页的指针数组，数组大小为vma的等效page个数；
            proc->pages = kzalloc(sizeof(proc->pages[0]) * ((vma->vm_end - vma->vm_start) / PAGE_SIZE), GFP_KERNEL);
            if (proc->pages == NULL) {
                ret = -ENOMEM;
                failure_string = "alloc page array";
                goto err_alloc_pages_failed;
            }
            proc->buffer_size = vma->vm_end - vma->vm_start;

            vma->vm_ops = &binder_vm_ops;
            vma->vm_private_data = proc;

            //分配物理页面，同时映射到内核空间和进程空间，先分配1个物理页 【见小节2.3.1】
            if (binder_update_page_range(proc, 1, proc->buffer, proc->buffer + PAGE_SIZE, vma)) {
                ret = -ENOMEM;
                failure_string = "alloc small buf";
                goto err_alloc_small_buf_failed;
            }
            buffer = proc->buffer; //binder_buffer对象 指向proc的buffer地址
            INIT_LIST_HEAD(&proc->buffers); //创建进程的buffers链表头
            list_add(&buffer->entry, &proc->buffers); //将binder_buffer地址 加入到所属进程的buffers队列
            buffer->free = 1;
            //将空闲buffer放入proc->free_buffers中
            binder_insert_free_buffer(proc, buffer);
            //异步可用空间大小为buffer总大小的一半。
            proc->free_async_space = proc->buffer_size / 2;
            barrier();
            proc->files = get_files_struct(current);
            proc->vma = vma;
            proc->vma_vm_mm = vma->vm_mm;
            return 0;

            ...// 错误flags跳转处，free释放内存之类的操作
            return ret;
        }

         */
    }
}
