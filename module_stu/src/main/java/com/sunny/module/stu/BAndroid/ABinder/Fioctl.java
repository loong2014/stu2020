package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class Fioctl extends StuImpl {

    /**
     * linux 提供的命令，不通的cmd对于不同的操作
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

    }
}
