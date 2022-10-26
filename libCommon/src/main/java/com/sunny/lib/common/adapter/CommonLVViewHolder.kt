package com.sunny.lib.common.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by zhangxin17 on 2020-01-16
 */
class CommonLVViewHolder(val context: Context, parent: ViewGroup?, layoutId: Int) {

    private lateinit var mConvertView: View
    private val mViews = SparseArray<View>()

    init {
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false)
        mConvertView.tag = this
    }

    companion object {
        fun get(
            context: Context,
            convertView: View?,
            parent: ViewGroup?,
            layoutId: Int
        ): CommonLVViewHolder {
            return if (convertView == null) {
                CommonLVViewHolder(context, parent, layoutId)
            } else {
                convertView.tag as CommonLVViewHolder
            }
        }
    }

    fun getConvertView(): View {
        return mConvertView
    }

    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews[viewId]

        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }

        return view as T
    }
}

