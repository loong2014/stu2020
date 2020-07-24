package com.sunny.family.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.sunny.aidl.IMyAidlInterface;

/**
 * Created by zhangxin17 on 2020/7/23
 */
public class SunService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void updateName(String name) throws RemoteException {

        }

        @Override
        public String getName() throws RemoteException {
            return null;
        }
    }

}
