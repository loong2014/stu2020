package com.sunny.family.detail.view.common;

import android.os.Parcel;
import android.os.Parcelable;

public class Poster implements Parcelable {

    /**
     * 正片
     */
    public static final String POSITIVE_VEDIO = "180001";
    /**
     * 预告
     */
    public static final String TRAILER_VEDIO = "180002";
    /**
     * 报道
     */
    public static final String REPORT_VEDIO = "182266";
    /**
     * 花絮
     */
    public static final String FEATHER_VEDIO = "180003";
    /**
     * 策划
     */
    public static final String SCHEAME_VEDIO = "182208";
    private String videoId;// 视频id
    private String categoryId;
    private String albumId;// 视频所属专辑id
    private int orderInAlbum;// 在专辑中顺序
    private boolean positive;// 是否是正片
    private String videoTypeId; //该视频是正片/预告/花絮/报道/策划
    private String episode;// 第几集、第几期
    private String img;  // 4:3
    private String name;// 视频名称
    private String subName;// 视频子名称
    private String desc;// 描述
    private long duration;// 时长
    private String guest;// 嘉宾
    private String singer;// 歌手
    private int dataType;// 类型
    private String ifCharge;// 是否是付费片源
    // 推荐相关字段
    private boolean is_rec;// 标志是否是推荐数据
    private String bucket;// 推荐的算法策略
    private String reid;// 推荐反馈的随机数
    private String areaRec;// 标志推荐页面功能区
    private String blocktype;// 推荐模块字段
    private String iconType;//角标
    private String bigImg = "";   // 3:4图片

    /**
     * 处理综艺月份划分
     */
    private int topStartPostion;
    private int topEndPostion;
    private int BottomPostion;
    private int varietyTopBottom;//-1其他；1-综艺往期节目 2-综艺年月份列表


    /**
     * 信息体对象
     */
    private PicInfo picInfo;
    private PicInfo picExtend;

    private String tag;
    /**
     * 统一跳转字段
     */
    private String jump;

    private String db_score; //豆瓣评分,  区别于tab里面人拼接   数据类型为 : 9.2
    public String score;

    private String externalAlbumId;
    private String externalVideoId;
    private int episodes;

    public String getExternalAlbumId() {
        return externalAlbumId;
    }

    public void setExternalAlbumId(String externalAlbumId) {
        this.externalAlbumId = externalAlbumId;
    }

    public String getExternalVideoId() {
        return externalVideoId;
    }

    public void setExternalVideoId(String externalVideoId) {
        this.externalVideoId = externalVideoId;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public Poster() {
        super();
    }

    public Poster(String videoId, String episode, String name, String ifCharge, String tag) {
        this.videoId = videoId;
        this.episode = episode;
        this.name = name;
        this.ifCharge = ifCharge;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void getSag(String t) {
        tag = t;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoId);
        dest.writeString(this.categoryId);
        dest.writeString(this.albumId);
        dest.writeInt(this.orderInAlbum);
        dest.writeByte((byte) (this.positive ? 1 : 0));
        dest.writeString(this.videoTypeId);
        dest.writeString(this.episode);
        dest.writeString(this.img);
        dest.writeString(this.name);
        dest.writeString(this.subName);

        dest.writeString(this.desc);
        dest.writeLong(this.duration);
        dest.writeString(this.guest);
        dest.writeString(this.singer);
        dest.writeInt(this.dataType);
        dest.writeString(this.ifCharge);
        dest.writeByte((byte) (this.is_rec ? 1 : 0));
        dest.writeString(this.bucket);
        dest.writeString(this.reid);
        dest.writeString(this.areaRec);

        dest.writeString(this.blocktype);
        dest.writeString(this.iconType);
        dest.writeString(this.bigImg);
        dest.writeParcelable(picInfo, flags);
        dest.writeParcelable(picExtend, flags);

        dest.writeString(this.tag);
        dest.writeString(this.jump);
        dest.writeString(this.db_score);
        dest.writeString(this.score);
    }

    public Poster(Parcel source) {
        super();
        videoId = source.readString();
        categoryId = source.readString();
        albumId = source.readString();
        orderInAlbum = source.readInt();
        positive = source.readByte() == (byte) 1;

        videoTypeId = source.readString();
        episode = source.readString();
        img = source.readString();
        name = source.readString();
        subName = source.readString();

        desc = source.readString();
        duration = source.readLong();
        guest = source.readString();
        singer = source.readString();
        dataType = source.readInt();

        ifCharge = source.readString();
        is_rec = source.readByte() == (byte) 1;
        bucket = source.readString();
        reid = source.readString();
        areaRec = source.readString();

        blocktype = source.readString();
        iconType = source.readString();
        bigImg = source.readString();

        picInfo = source.readParcelable(PicInfo.class.getClassLoader());
        picExtend = source.readParcelable(PicInfo.class.getClassLoader());

        tag = source.readString();
        jump = source.readString();

        db_score = source.readString();
        score = source.readString();
    }

    public static final Parcelable.Creator<Poster> CREATOR = new Parcelable.Creator<Poster>() {

        @Override
        public Poster createFromParcel(Parcel source) {
            return new Poster(source);
        }

        @Override
        public Poster[] newArray(int size) {
            return new Poster[size];
        }
    };

    public PicInfo getPicInfo() {
        return this.picInfo;
    }

    public void setPicInfo(PicInfo picInfo) {
        this.picInfo = picInfo;
    }

    public PicInfo getPicExtend() {
        return this.picExtend;
    }

    public void setPicExtend(PicInfo picExtend) {
        this.picExtend = picExtend;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getOrderInAlbum() {
        return this.orderInAlbum;
    }

    public void setOrderInAlbum(int orderInAlbum) {
        this.orderInAlbum = orderInAlbum;
    }

    public String getVideoTypeId() {
        return this.videoTypeId;
    }

    public void setVideoTypeId(String videoTypeId) {
        this.videoTypeId = videoTypeId;
    }

    public String getEpisode() {
        return this.episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return this.subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getGuest() {
        return this.guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getSinger() {
        return this.singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public boolean isPositive() {
        return this.positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public int getDataType() {
        return this.dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public boolean isIs_rec() {
        return this.is_rec;
    }

    public void setIs_rec(boolean is_rec) {
        this.is_rec = is_rec;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getReid() {
        return this.reid;
    }

    public void setReid(String reid) {
        this.reid = reid;
    }

    public String getAreaRec() {
        return this.areaRec;
    }

    public void setAreaRec(String areaRec) {
        this.areaRec = areaRec;
    }

    public String getBlocktype() {
        return this.blocktype;
    }

    public void setBlocktype(String blocktype) {
        this.blocktype = blocktype;
    }

    public String getIfCharge() {
        return this.ifCharge;
    }

    public void setIfCharge(String ifCharge) {
        this.ifCharge = ifCharge;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }


    public String getBigImg() {
        return bigImg.trim();
    }

    public void setBigImg(String bigImg) {
        this.bigImg = bigImg;
    }

    public String getDb_score() {
        return db_score;
    }

    public void setDb_score(String db_score) {
        this.db_score = db_score;
    }

    public int getTopStartPostion() {
        return topStartPostion;
    }

    public void setTopStartPostion(int topStartPostion) {
        this.topStartPostion = topStartPostion;
    }

    public int getTopEndPostion() {
        return topEndPostion;
    }

    public void setTopEndPostion(int topEndPostion) {
        this.topEndPostion = topEndPostion;
    }

    public int getBottomPostion() {
        return BottomPostion;
    }

    public void setBottomPostion(int bottomPostion) {
        BottomPostion = bottomPostion;
    }

    public int getVarietyTopBottom() {
        return varietyTopBottom;
    }

    public void setVarietyTopBottom(int varietyTopBottom) {
        this.varietyTopBottom = varietyTopBottom;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "videoId='" + videoId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", albumId='" + albumId + '\'' +
                ", orderInAlbum=" + orderInAlbum +
                ", positive=" + positive +
                ", videoTypeId='" + videoTypeId + '\'' +
                ", episode='" + episode + '\'' +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", subName='" + subName + '\'' +
                ", desc='" + desc + '\'' +
                ", duration=" + duration +
                ", guest='" + guest + '\'' +
                ", singer='" + singer + '\'' +
                ", dataType=" + dataType +
                ", ifCharge='" + ifCharge + '\'' +
                ", is_rec=" + is_rec +
                ", bucket='" + bucket + '\'' +
                ", reid='" + reid + '\'' +
                ", areaRec='" + areaRec + '\'' +
                ", blocktype='" + blocktype + '\'' +
                ", iconType='" + iconType + '\'' +
                ", bigImg='" + bigImg + '\'' +
                ", picInfo=" + picInfo +
                ", picExtend=" + picExtend +
                ", tag='" + tag + '\'' +
                ", jump='" + jump + '\'' +
                ", db_score=" + db_score +
                ", score=" + score +
                '}';
    }
}