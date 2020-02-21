package com.sunny.family.cityexpan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.sunny.family.R;
import com.sunny.lib.city.CityInfo;

import java.util.List;

public class ExpandableCityAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<CityInfo> mCityList;

    private IClickListener mClickListener;

    public ExpandableCityAdapter(Context context) {
        mContext = context;
    }

    public void setCityList(List<CityInfo> list) {
        mCityList = list;
    }

    public void setClickListener(IClickListener listener) {
        mClickListener = listener;
    }

    /**
     * 返回第一级List长度
     */
    @Override
    public int getGroupCount() {
        if (mCityList != null) {
            return mCityList.size();
        }
        return 0;
    }

    /**
     * 返回指定groupPosition的第二级List长度
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (mCityList == null || mCityList.size() <= groupPosition)
            throw new IndexOutOfBoundsException("invalid position!");

        List<CityInfo> childList = mCityList.get(groupPosition).getChildren();
        if (childList != null) {
            return childList.size();
        }
        return 0;
    }

    /**
     * 返回一级List里的内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mCityList.get(groupPosition);
    }

    /**
     * 返回二级List的内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCityList.get(groupPosition).getChildren().get(childPosition);
    }

    /**
     * 返回一级View的id 保证id唯一
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 返回二级View的id 保证id唯一
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    /**
     * 指示在对基础数据进行更改时子ID和组ID是否稳定
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.block_city_tip, null);
            groupHolder = new GroupHolder();
            groupHolder.nameTv = convertView.findViewById(R.id.tv_city_tip);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        if (isExpanded) {
            convertView.setBackgroundColor(Color.WHITE);
            groupHolder.nameTv.setTextColor(Color.BLACK);
        } else {
            convertView.setBackgroundColor(Color.GRAY);
            groupHolder.nameTv.setTextColor(Color.WHITE);
        }

        groupHolder.nameTv.setText(mCityList.get(groupPosition).getName());

        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.block_city, null);
            itemHolder = new ItemHolder();
            itemHolder.nameTv = convertView.findViewById(R.id.tv_city_name);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.nameTv.setTextColor(Color.YELLOW);

        final CityInfo cityInfo = mCityList.get(groupPosition).getChildren().get(childPosition);

        itemHolder.nameTv.setText(cityInfo.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onChildViewClick(groupPosition, childPosition, cityInfo);
                }
            }
        });

        return convertView;
    }


    class GroupHolder {
        public TextView nameTv;
    }

    class ItemHolder {
        public TextView nameTv;
    }

    interface IClickListener {

        void onChildViewClick(int groupPosition, int childPosition, CityInfo cityInfo);

    }
}
