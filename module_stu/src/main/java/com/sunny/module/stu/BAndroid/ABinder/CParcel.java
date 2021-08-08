package com.sunny.module.stu.BAndroid.ABinder;

import android.os.Parcel;

import com.sunny.module.stu.base.StuImpl;

public class CParcel extends StuImpl {

    /**
     * binder 是 android 提供的一个容器，主要用于 binder 的数据传输
     * 支持序列化，以及反序列化
     */
    @Override
    public void 是什么() {

        Parcel parcel = Parcel.obtain();

        // 数据写入
        parcel.writeString("张三");
        parcel.writeInt(18);

        // 序列化
        byte[] data = parcel.marshall();

        // 使用完要进行回收
        parcel.recycle();

        ////////////////

        // 读数据
        parcel = Parcel.obtain();

        // 反序列化
        parcel.unmarshall(data, 0, data.length);

        // 数据读取 一定要按照写入的顺序进行读取
        String name = parcel.readString();
        int age = parcel.readInt();

        parcel.recycle();
    }

}
