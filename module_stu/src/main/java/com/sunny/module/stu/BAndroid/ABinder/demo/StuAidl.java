package com.sunny.module.stu.BAndroid.ABinder.demo;

import android.os.IBinder;
import android.os.RemoteException;


public class StuAidl {

    ISunny sunny = new ISunny() {
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

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
}
