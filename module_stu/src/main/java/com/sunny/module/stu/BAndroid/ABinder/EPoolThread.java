package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class EPoolThread extends StuImpl {

    /**
     *创建线程池，其实就只是创建一个线程，该PoolThread继承Thread类。t->run()方法最终调用内部类 PoolThread的threadLoop()方法。
     */
    @Override
    public void 是什么() {

    }

    private void _threadLoop(){
        /*
protected:
    virtual bool threadLoop()
    {
        IPCThreadState::self()->joinThreadPool(mIsMain);
        return false;
    }

    const bool mIsMain;
};
         */
    }
}
