package com.sunny.module.stu.book;

import android.os.Parcel;
import android.os.Parcelable;

public class XlhDemo implements Parcelable {


    private String name;
    private int age;
    private float money;


    protected XlhDemo(Parcel in) {
        name = in.readString();
        age = in.readInt();
        money = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeFloat(money);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<XlhDemo> CREATOR = new Creator<XlhDemo>() {
        @Override
        public XlhDemo createFromParcel(Parcel in) {
            return new XlhDemo(in);
        }

        @Override
        public XlhDemo[] newArray(int size) {
            return new XlhDemo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

}
