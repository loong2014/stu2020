package com.sunny.module.stu.BAndroid.ABinder.demo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.sunny.module.stu.BAndroid.ABinder.demo.ISunny;

public class SunnyService extends Service {

    private final ISunny.Stub mBinder = new ISunny.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void updateName(String name) throws RemoteException {
            int callingUid = getCallingUid();
            if (callingUid != 1001) {
                // 如果调用者的 UID 不是 1001，就抛出一个安全异常。
                throw new SecurityException("Invalid UID. Only the app with UID 1001 is allowed to access this service.");
            }
        }

        @Override
        public String getName() throws RemoteException {
            return null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 检查调用者是否有权限
        if (checkCallingOrSelfPermission("com.example.MY_PERMISSION") != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        mBinder.linkToDeath(mDeathRecipient, 0);
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 默认START_STICKY，被kill重启; START_NOT_STICKY表示被kill后不重启
        return Service.START_NOT_STICKY;
    }
}
