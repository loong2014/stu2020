package com.sunny.family.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.sunny.aidl.IMyAidlInterface;

/**
 * Created by zhangxin17 on 2020/7/23
 */
public class SunServiceModel {

    private IMyAidlInterface mAidlInterface;

    public SunServiceModel() {

    }

    public void doBindService(Context context) {

        Intent intent = new Intent("com.sunny.family.SunService");

        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public String getName() {
        if (mAidlInterface == null) {
            return "";
        }

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

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidlInterface = IMyAidlInterface.Stub.asInterface(service);

            // 设置死亡代理
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 判断binder是否死亡
            service.isBinderAlive();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}
