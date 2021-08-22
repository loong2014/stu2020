package com.sunny.module.stu.BAndroid.ABinder.驱动;

import com.sunny.module.stu.base.StuImpl;

/**
 * http://gityuan.com/2015/11/01/binder-driver/
 */
public class Stu_binder驱动 extends StuImpl {
    @Override
    public void a_是什么() {
        //
        /*
        Binder驱动是Android专用的，但底层的驱动架构与Linux驱动一样。
            binder驱动在以misc设备进行注册，作为虚拟字符设备，没有直接操作硬件，只是对设备内存的处理。
            主要是驱动设备的
                初始化(binder_init)，
                打开(binder_open)，
                映射(binder_mmap)，
                数据操作(binder_ioctl)。
         */

        // 注册binder驱动
        binder_init();

        // 创建 binder_proc 对象
        binder_open();

        // 建立 用户空间 和 内核空间 的虚拟映射
        binder_mmap();

    }

    @Override
    public void s_数据结构() {
    }

    /**
     * 内核启动{@link com.sunny.module.stu.GLinux系统.Stu_内核启动流程}的时候调用
     * <p>
     * 注册misc设备{@link com.sunny.module.stu.GLinux系统.Stu_misc设备}
     */
    private void binder_init() {

        // 主要工作是为了注册misc设备
        /*
        misc_register(&binder_miscdev);
         */

        // struct miscdevice binder_miscdev
        /*
        static struct miscdevice binder_miscdev = {
            .minor = MISC_DYNAMIC_MINOR,    //次设备号 动态分配
            .name = "binder",               //设备名
            .fops = &binder_fops            //设备的文件操作结构，这是file_operations结构
        };
         */

        // struct file_operations binder_fops
        /*
        static const struct file_operations binder_fops = {
            .owner              = THIS_MODULE,
            .poll               = binder_poll,
            .unlocked_ioctl     = binder_ioctl,
            .compat_ioctl       = binder_ioctl,
            .mmap               = binder_mmap,
            .open               = binder_open,
            .flush              = binder_flush,
            .release            = binder_release,
        };
         */
    }

    /**
     * ProcessState::ProcessState(): mDriverFD(open_driver()) 的 open_driver 中打开驱动设备
     * mDriverFD = open("/dev/binder", O_RDWR);
     * <p>
     * 打开驱动设备，一个进程对应一个 binder_proc 结构体。
     * binder_open 就是构建 binder_proc 对象的，并将其添加到 binder_procs 为表头的队列中
     */
    private void binder_open() {
        // binder_proc 结构体
        /*
        binder_proc结构体：用于管理IPC所需的各种信息，拥有其他结构体的结构体。
         */

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
         */
    }

    /**
     * 主要功能：首先在内核虚拟地址空间，申请一块与用户虚拟内存相同大小的内存；
     * 然后再申请1个page大小的物理内存，再将同一块物理内存分别映射到
     * 【内核虚拟地址空间】和【用户虚拟内存空间】，
     * 从而实现了用户空间的Buffer和内核空间的Buffer同步操作的功能。
     */
    private void binder_mmap() {

        /*
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
