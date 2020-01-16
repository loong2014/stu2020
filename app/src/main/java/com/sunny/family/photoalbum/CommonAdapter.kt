package com.sunny.family.photoalbum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sunny.family.R
import com.sunny.family.model.MediaModel
import com.sunny.lib.utils.ContextProvider

abstract class CommonAdapter<T>(context: Context?, dataList: List<T>?, viewLayoutId: Int) : BaseAdapter() {

    private val mContext = context ?: ContextProvider.appContext
    private val mDataList: List<T> = dataList ?: mutableListOf()
    private val mViewLayoutId = viewLayoutId
    private val mInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): T? {
        return mDataList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: MyViewHolder
        var v: View
        if (convertView == null) {
            holder = MyViewHolder()

            v = mInflater.inflate(mViewLayoutId, parent, false)

            holder.iv_img = v.findViewById(R.id.item_img)
            holder.tv_name = v.findViewById(R.id.item_name)

            // 设置tag
            v.tag = holder
        } else {

            v = convertView
            // 获取tag
            holder = v.tag as MyViewHolder
        }

        // 填充数据
        getItem(position)?.let {
            val data = it as MediaModel

            holder.tv_name.text = data.name

            Glide.with(mContext).load(data.path).into(holder.iv_img)
        }

        return v
    }

    class MyViewHolder {
        lateinit var iv_img: ImageView
        lateinit var tv_name: TextView
    }

}