package com.sunny.module.stu.BAndroid.ABinder;

import com.sunny.module.stu.base.StuImpl;

public class Stu_CS模式 extends StuImpl {

    @Override
    public void c_功能() {
        /*
        BpBinder(客户端)和BBinder(服务端)都是Android中Binder通信相关的代表，它们都从IBinder类中派生而来。

        client端：BpBinder.transact()来发送事务请求；

        server端：BBinder.onTransact()会接收到相应事务。

         */
    }
}
