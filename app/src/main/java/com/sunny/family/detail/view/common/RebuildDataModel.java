package com.sunny.family.detail.view.common;

import java.util.List;

/**
 * uiType对应的通用数据结构
 * Created by ZhangBoshi
 */
public class RebuildDataModel {
    /**
     * {@link List < RebuildDataModel >}中根据uiType归类，idx为data在某个uiType数据列表中的位置信息，idx从0开始
     */
    private int idx;
    private int uiType;
    private String name;
    private String dataSource;
    private Object data;

    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RebuildDataModel))
            return false;

        RebuildDataModel that = (RebuildDataModel) o;

        if (uiType != that.uiType)
            return false;
        if (idx != that.idx)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (dataSource != null ? !dataSource.equals(that.dataSource) : that.dataSource != null)
            return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = uiType;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dataSource != null ? dataSource.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + idx;
        return result;
    }

    @Override
    public String toString() {
        return "RebuildDataModel{" + "uiType=" + uiType + ", name='" + name + '\''
                + ", dataSource='" + dataSource + '\'' + ", data=" + data + ", idx=" + idx + '}';
    }

    /**
     * 用于BlockDiffCallback，不判断data字段，新增字段需要在此体现
     * @param o
     * @return
     */
    public boolean equalsPart(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RebuildDataModel))
            return false;

        RebuildDataModel that = (RebuildDataModel) o;

        if (uiType != that.uiType)
            return false;
        if (idx != that.idx)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        return dataSource != null ? dataSource.equals(that.dataSource) : that.dataSource == null;
    }
}
