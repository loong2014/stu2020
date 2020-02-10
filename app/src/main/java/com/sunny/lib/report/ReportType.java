package com.sunny.lib.report;

import android.text.TextUtils;

/**
 * Created by zhangxin17 on 2019/8/24
 */
public class ReportType {

    // 角标类型
    public class IconType {
        public static final String NOICON = "0";// None(无角标)
        public static final String ALL = "1";// External(全网(外网))
        public static final String PAY = "2";// Charge(付费)
        public static final String VIP = "3";// VIP(会员)
        public static final String EXCLUSIVE = "4";// Exclusive(独播)
        public static final String HOMEMADE = "5";// HOMEMADE(自制)
        public static final String SPECIAL = "6";// Subject(专题)
        public static final String PREVIEW = "7";// PREVIEW(预告)
        public static final String K4 = "8";// 4K
        public static final String K2 = "9";// 2K
        public static final String P1080 = "10";// 1080P
        public static final String DTS = "11";// DTS(影院音)
        public static final String HUAXU = "12";// HUAXU(花絮)
        public static final String LIVE = "13";// Live(直播)
        public static final String D3 = "14";// 3d
        public static final String DU = "15";// Duby(杜比)
        public static final String CID = "16";// CID(按影视分类catagory)
        public static final String TVOD = "18";// TVOD(点播)
        public static final String TIME_FREE = "19";// 限免角标
        public static final String VIP_HOME = "20";// 家庭会员角标
        public static final String TVOD_COURSE = "21";// 课程TVOD
        public static final String TRY_FREE = "22";// 免费
        public static final String TRY_PLAY = "23";// 试看，视频免费，专辑付费
        public static final String COURSE = "62";// 课程付费角标
    }

    /**
     * 是否付费
     */
    public class ChargeType {
        public static final String FREE = "0"; // 免费
        public static final String CHARGE = "1"; // 付费
    }

    // 专辑类型
    public class AlbumType {
        public static final int NORMAL = 1;// 普通专辑
        public static final int COURSE = 2;// 课程专辑
        public static final int SINGLE = 3;// 单点付费专辑
    }

    // 专辑分类
    public class CategoryType {
        public static final int FILM = 1;// 电影
        public static final int TV = 2;// 电视剧
        public static final int ENT = 3;// 娱乐
        public static final int SPORT = 4;// 体育
        public static final int CARTOON = 5;// 动漫
        public static final int ZIXUN = 1009;// 资讯
        public static final int YUAN_CHUANG = 7;// 原创
        public static final int OTHER = 8;// 其他
        public static final int MUSIC = 9;// 音乐
        public static final int FUNNY = 10;// 搞笑
        public static final int VARIETY = 11;// 综艺
        public static final int KE_JIAO = 12;// 科教
        public static final int SHENG_HUO = 13;// 生活
        public static final int CAR = 14;// 汽车
        public static final int DFILM = 16;// 纪录片
        public static final int GONG_KAI_KE = 17;// 公开课
        public static final int LETV_MADE = 19;// 乐视制造
        public static final int FENG_SHANG = 20;// 风尚
        public static final int CAI_JING = 22;// 财经
        public static final int TRAVEL = 23;// 旅游
        public static final int HOTSPOT = 30;// 热点
        public static final int QU_YI = 32;// 曲艺
        public static final int XI_QU = 33;// 戏曲
        public static final String PARENTING = "34";// 亲子
        public static final int AD = 36;// 广告
        public static final int SHOPPING = 39;// 商业
        public static final String COURSE = "62";// 课程
        public static final int TEACH = 1021;// 教育
        public static final int TEACH_CHILD = 542015;// 教育频道下的幼儿用作乐视儿童
    }

    // 课程类型
    public class CourseType {
        public static final String CHILD = "870001"; // 儿童课程
        public static final String PARENT = "870002"; // 家长课程
    }

    public static int getAlbumType(String categoryId) {

        if (categoryId == null) {
            return AlbumType.NORMAL;
        }

        if (CategoryType.COURSE.equals(categoryId)) {
            return AlbumType.COURSE;
        }

        if (CategoryType.PARENTING.equals(categoryId)) {
            return AlbumType.SINGLE;
        }

        return AlbumType.NORMAL;
    }

    public static boolean isSingleAlbum(String categoryId) {
        if (categoryId == null) {
            return false;
        }

        if (CategoryType.COURSE.equals(categoryId)) {
            return true;
        }

        if (CategoryType.PARENTING.equals(categoryId)) {
            return true;
        }

        return false;
    }

    // 视频来源，默认来源是letv
    public class SourceType {
        public static final String SOURCE_MIX = "mix"; // 混合
        public static final String SOURCE_LETV = "letv"; // 乐视
        public static final String SOURCE_TENCENT = "tencent"; // 腾讯
        public static final String SOURCE_MGTVOTT = "mgtvott"; // 芒果
        public static final String SOURCE_WASUTV = "wasutv"; // 华视
    }

    public static boolean isLetvSource(String source) {
        if (TextUtils.isEmpty(source)) {
            return true;
        }
        return SourceType.SOURCE_LETV.equals(source);
    }

    public static boolean isTencentSource(String source) {
        return SourceType.SOURCE_TENCENT.equals(source);
    }
}
