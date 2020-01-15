package com.sunny.family.photoalbum

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class CommonAdapter<T>(context: Context?, dataList: List<T>) : BaseAdapter() {

    private var mDatas: List<T>? = mutableListOf()

    init {
        mDatas = dataList
    }

    override fun getCount(): Int {
        return if (mDatas != null) {
            mDatas!!.size
        } else {
            0
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): T? {
        return if (count == 0) {
            null
        } else {
            mDatas?.get(position)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}