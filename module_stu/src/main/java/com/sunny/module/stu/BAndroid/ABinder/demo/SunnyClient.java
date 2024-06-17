package com.sunny.module.stu.BAndroid.ABinder.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class SunnyClient {

    private ISunny mAidlInterface;

    public SunnyClient() {

    }

    private void doStartActivity(Context context,Intent intent){
        context.startActivity(intent);
    }

    public String getName() {
        if (mAidlInterface == null) {
            return "";
        }

        MediaPlayer mediaPlayer  = new MediaPlayer();
        try {
            return mAidlInterface.getName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void updateName(String name) {
        if (mAidlInterface == null) {
            return;
        }

        try {
            mAidlInterface.updateName(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getService(Context context) {
        context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void doBindService(Context context) {

        Intent intent = new Intent("com.sunny.family.SunService");

        // 使用 Bundle 传递客户端的 IBinder 对象，服务端可以通过获取clientBinder的binder来判断客户端是否挂了
        Bundle bundle = new Bundle();
        bundle.putBinder("clientBinder", new Binder());
        intent.putExtras(bundle);

        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidlInterface = ISunny.Stub.asInterface(service);

            // 设置死亡代理
            try {
                service.linkToDeath(mDeathRecipient, 0);
                mAidlInterface.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 判断binder是否死亡
            service.isBinderAlive();
            mAidlInterface.asBinder().isBinderAlive();

            // 进行binder通信
            try {
                mAidlInterface.updateName("client123");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidlInterface = null;
        }
    };
}
