package com.sunny.family.detail.model;

import com.sunny.family.detail.view.common.Poster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther:libenqi
 * @date:2020/01/02
 * @email: libenqi1@le.com
 * @description:
 */
public class DetailModelResult extends BaseResult {
    public DetailModel data;

    public static class DetailModel {
        private static final long serialVersionUID = 7529960530617665994L;

        //专辑ID
        private String albumId; //专辑id
        private String categoryId; // 专辑分类id   1 电视剧; 2电影; 3 动漫; 4: 综艺; 5音乐; .... >>> http://wiki.letv.cn/pages/viewpage.action?pageId=71344381
        private String subCategoryName; //子分类(标签)名称
        private String img; //焦点图
        private String name; //专辑名称
        private String alias; //专辑别名
        private String description; //专辑描述
        private boolean positive; //是否正片
        private String areaName; //地区
        private String releaseDate; //发行日期/上映日期(详情页界面显示用)
        private long vv; //专辑观看次数
        private long commentCnt; //评论数
        private List<Poster> positiveSeries = new ArrayList<Poster>();
        ; //剧集列表（正片）
        private List<Poster> preSeries = new ArrayList<Poster>();  //预告片集合
        private List<Poster> attachingSeries = new ArrayList<Poster>();  //综艺周边看点    2.13版本中为了静态接口兼容老版本，添加新字段实现综艺预告
        private List<Poster> segments; //片段
        private List<Poster> positiveAddSeries = new ArrayList<Poster>(); //剧集列表扩展（预告、抢先看等）
        private String compere; //主持人信息
        private String playTvName; //播出电视台名称
        private List<Poster> relation = new ArrayList<Poster>(); //根据当前专辑相关信息，通过推荐系统获取到的相关内容
        private String dataType; //跳转类型
        private String episodes; //总集数
        private String updateFrequency; //更新频率
        private String fitAge; //适应年龄
        private String duration;//时长，lecom版本返回毫秒
        private boolean end; //是否跟播剧
        private String playPlatform; //允许播放平台
        private String nowEpisode; //更新至x集
        private String nowIssue; //更新至x期
        private String score; //评分
        private Map<String, String> downloadPlatform; //允许下载平台
        private String varietyShow; //是否展示成综艺样式（或，Lecon需求--是否按“剧集”展示）,1是 0否
        private String singer; //歌手
        private boolean charge = false; //是否收费   通过chargeInfos 返回值本地判断
        private String subName;// 专辑子名称
        private String iconType;//角标类型
        private String bigImg;//头信息大背景海报
        private List<String> tags = new ArrayList<String>(); //标签组: 文本中插入的{icontype}为图标占位符，例如 {score}－豆瓣评分，{rank}－TOP排名，{vcount}－播放次数
        private String db_score; //豆瓣评分,  区别于tab里面人拼接   数据类型为 : 9.2

        private String channelId;
        private String srcType;
        // 视频来源
        public String source;
        //第三方专辑ID
        public String externalAlbumId;

//    public DetailModel(){}

        public String getSubName() {
            return this.subName;
        }

        public void setSubName(String subName) {
            this.subName = subName;
        }

        public String getAlbumId() {
            return this.albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getCategoryId() {
            return this.categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getSubCategoryName() {
            return this.subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
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

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAreaName() {
            return this.areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public long getVv() {
            return this.vv;
        }

        public void setVv(long vv) {
            this.vv = vv;
        }

        public long getCommentCnt() {
            return this.commentCnt;
        }

        public void setCommentCnt(long commentCnt) {
            this.commentCnt = commentCnt;
        }

        public List<Poster> getPositiveSeries() {
            return this.positiveSeries;
        }

        public void setPositiveSeries(List<Poster> positiveSeries) {
            this.positiveSeries = positiveSeries;
        }

        public List<Poster> getPreSeries() {
            return this.preSeries;
        }

        public void setPreSeries(List<Poster> preSeries) {
            this.preSeries = preSeries;
        }

        public List<Poster> getSegments() {
            return this.segments;
        }

        public void setSegments(List<Poster> segments) {
            this.segments = segments;
        }

        public String getCompere() {
            return this.compere;
        }

        public void setCompere(String compere) {
            this.compere = compere;
        }

        public String getPlayTvName() {
            return this.playTvName;
        }

        public void setPlayTvName(String playTvName) {
            this.playTvName = playTvName;
        }

        public List<Poster> getRelation() {
            return this.relation;
        }

        public void setRelation(List<Poster> relation) {
            this.relation = relation;
        }

        public String getDataType() {
            return this.dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getReleaseDate() {
            return this.releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getEpisodes() {
            return this.episodes;
        }

        public void setEpisodes(String episodes) {
            this.episodes = episodes;
        }

        public String getUpdateFrequency() {
            return this.updateFrequency;
        }

        public void setUpdateFrequency(String updateFrequency) {
            this.updateFrequency = updateFrequency;
        }

        public String getFitAge() {
            return this.fitAge;
        }

        public void setFitAge(String fitAge) {
            this.fitAge = fitAge;
        }

        public String getDuration() {
            return this.duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isPositive() {
            return this.positive;
        }

        public void setPositive(boolean positive) {
            this.positive = positive;
        }

        public boolean isEnd() {
            return this.end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getPlayPlatform() {
            return this.playPlatform;
        }

        public void setPlayPlatform(String playPlatform) {
            this.playPlatform = playPlatform;
        }

        public String getNowEpisode() {
            return this.nowEpisode;
        }

        public void setNowEpisode(String nowEpisode) {
            this.nowEpisode = nowEpisode;
        }

        public String getNowIssue() {
            return this.nowIssue;
        }

        public void setNowIssue(String nowIssue) {
            this.nowIssue = nowIssue;
        }

        public Map<String, String> getDownloadPlatform() {
            return this.downloadPlatform;
        }

        public void setDownloadPlatform(Map<String, String> downloadPlatform) {
            this.downloadPlatform = downloadPlatform;
        }

        public String getScore() {
            return this.score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getVarietyShow() {
            return this.varietyShow;
        }

        public void setVarietyShow(String varietyShow) {
            this.varietyShow = varietyShow;
        }

        public String getSinger() {
            return this.singer;
        }

        public void setSinger(String singer) {
            this.singer = singer;
        }

        public boolean isCharge() {
            return this.charge;
        }

        public String getIconType() {
            return iconType;
        }

        public void setIconType(String iconType) {
            this.iconType = iconType;
        }

        public void setCharge(boolean charge) {
            this.charge = charge;
        }

        public List<Poster> getPositiveAddSeries() {
            return this.positiveAddSeries;
        }

        public void setPositiveAddSeries(List<Poster> positiveAddSeries) {
            this.positiveAddSeries = positiveAddSeries;
        }

        public String getBigImg() {
            return bigImg;
        }

        public void setBigImg(String bigImg) {
            this.bigImg = bigImg;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<Poster> getAttachingSeries() {
            return attachingSeries;
        }

        public void setAttachingSeries(List<Poster> attachingSeries) {
            this.attachingSeries = attachingSeries;
        }

        public String getDb_score() {
            return db_score;
        }

        public void setDb_score(String db_score) {
            this.db_score = db_score;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getSrcType() {
            return srcType;
        }

        public void setSrcType(String srcType) {
            this.srcType = srcType;
        }

        public DetailModel() {
            super();
        }

    }
}
