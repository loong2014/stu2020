package com.sunny.other;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangxin17 on 2020-04-27
 */
public class ParcelableDemo implements Parcelable {

    private String name;
    private int age;
    private boolean gender;

    public ParcelableDemo() {

    }

    protected ParcelableDemo(Parcel in) {
        name = in.readString();
        age = in.readInt();
        gender = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeByte((byte) (gender ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableDemo> CREATOR = new Creator<ParcelableDemo>() {
        @Override
        public ParcelableDemo createFromParcel(Parcel in) {
            return new ParcelableDemo(in);
        }

        @Override
        public ParcelableDemo[] newArray(int size) {
            return new ParcelableDemo[size];
        }
    };
}
