package com.sunny.family.detail.model;

import com.sunny.family.detail.view.common.Poster;

import java.util.List;

/**
 * @auther:libenqi
 * @date:2019/05/15
 * @email: libenqi1@le.com
 * @description:2.20 详情页更多推荐接口的新bean
 */
public class DetailModelMoreResult extends BaseResult {
    public DetailModelMore data;

    public static class DetailModelMore {
        //列表
        public List<NodeBean> dataList;
        public long dateTime;
    }


    public static class NodeBean {
        public List<Poster> albumBlockData;
        public String name;
        public String attrName;
        public String tid;
    }
}
