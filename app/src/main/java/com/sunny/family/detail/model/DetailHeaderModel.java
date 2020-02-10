package com.sunny.family.detail.model;


/**
 * @auther:libenqi
 * @date:2019/12/13
 * @email: libenqi1@le.com
 * @description:
 */
public class DetailHeaderModel {
    private DetailModelMoreResult.DetailModelMore detailModelMore;
    private DetailModelResult.DetailModel detailModel;

    public DetailHeaderModel(DetailModelMoreResult.DetailModelMore modelMore, DetailModelResult.DetailModel detailModel) {
        this.detailModelMore = modelMore;
        this.detailModel = detailModel;
    }


    public DetailModelMoreResult.DetailModelMore getDynamicResult() {
        return detailModelMore;
    }

    public void setDynamicResult(DetailModelMoreResult.DetailModelMore modelMore) {
        this.detailModelMore = modelMore;
    }

    public DetailModelResult.DetailModel getDetailModel() {
        return detailModel;
    }

    public void setDetailModel(DetailModelResult.DetailModel detailModel) {
        this.detailModel = detailModel;
    }
}
