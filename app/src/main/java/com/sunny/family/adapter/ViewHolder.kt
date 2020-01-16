package com.sunny.family.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by zhangxin17 on 2020-01-16
 */
class ViewHolder(val context: Context, parent: ViewGroup?, layoutId: Int) {

    private lateinit var mConvertView: View
    private val mViews = SparseArray<View>()

    init {
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false)
        mConvertView.tag = this
    }

    companion object {
        fun get(context: Context, convertView: View?, parent: ViewGroup?, layoutId: Int): ViewHolder {
            return if (convertView == null) {
                ViewHolder(context, parent, layoutId)
            } else {
                convertView.tag as ViewHolder
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

    fun setText(viewId: Int, text: String): ViewHolder {
        val tv: TextView = getView(viewId)
        tv.text = text
        return this
    }

    fun setImage(viewId: Int, url: String): ViewHolder {
        val iv: ImageView = getView(viewId)
        Glide.with(context).load(url).into(iv)
        return this
    }
}

