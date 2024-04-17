package com.sunny.module.stu.BAndroid.事件分发

import com.sunny.module.stu.base.StuImpl

class StuTouchEvent : StuImpl() {
    override fun p_流程() {
        // 生成事件
        // EventHub通过epoll机制监听/dev/input下的设备节点，生成RawEvent事件
        // EventEntry并发送到InputDispatch线程

        // 读取事件
        // InputReader 线程循环调用loopOnce方法，从EventHub读取RawEvent事件并经过处理生成EventEntry事件，并发送到InputDispatch线程的mInBoundQueue队列

        // 分发事件
        // InputDispatch 线程循环调用dispatchOnce方法，监听mInBoundQueue队列，获取EventEntry事件

        // ViewRootImpl dispatchInputEvent
        // down
        // move
        // up
    }

    private fun sut_TouchEvent() {


    }
}