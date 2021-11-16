package com.sunny.module.stu.BAndroid.ANR;

import com.sunny.module.stu.base.StuImpl;

/*
http://gityuan.com/2016/07/02/android-anr/

http://gityuan.com/2019/04/06/android-anr/
 */
public class StuANR extends StuImpl {
    @Override
    public void a_是什么() {
        /*
        ANR，Application Not Responding
        当应用程序一段时间无法及时响应，则会弹出ANR对话框，让用户选择继续等待，还是强制关闭。

        ANR是一套监控Android应用响应是否及时的机制

        常见的ANR有service、broadcast、provider以及input:
            Service Timeout:比如前台服务在20s内未执行完成；
            BroadcastQueue Timeout：比如前台广播在10s内未执行完成
            ContentProvider Timeout：内容提供者,在publish过超时10s;
            InputDispatching Timeout: 输入事件分发超时5s，包括按键和触摸事件。
         */
    }
}
