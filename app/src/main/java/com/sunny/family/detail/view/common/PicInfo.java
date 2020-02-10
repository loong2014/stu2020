package com.sunny.family.detail.view.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信息体
 */
public class PicInfo implements Parcelable {
    private static final long serialVersionUID = 8333463494487796897L;
    public static final String RIGHT_TOP_NO_TYPE = "0";
    public static final String RIGHT_TOP_TIME_TYPE = "1";
    public static final String RIGHT_TOP_SCORE_TYPE = "2";
    /** 左上角 */
    private String left;
    /** 右上角 */
    private String right;
    /** title 标题 */
    private String top;
    /** subTitle 副标题 */
    private String bottom;
    /** 右上角类型：0：没有值、1：时间、2：得分 */
    private String rt;

    public String getRt() {
        return this.rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getLeft() {
        return this.left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return this.right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getTop() {
        return this.top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return this.bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.left);
        dest.writeString(this.right);
        dest.writeString(this.top);
        dest.writeString(this.bottom);
        dest.writeString(this.rt);
    }

    public static final Creator<PicInfo> CREATOR = new Creator<PicInfo>() {

        @Override
        public PicInfo createFromParcel(Parcel source) {
            PicInfo picInfo = new PicInfo();
            picInfo.setLeft(source.readString());
            picInfo.setRight(source.readString());
            picInfo.setTop(source.readString());
            picInfo.setBottom(source.readString());
            picInfo.setRt(source.readString());
            return picInfo;
        }

        @Override
        public PicInfo[] newArray(int size) {
            return new PicInfo[size];
        }
    };
}
