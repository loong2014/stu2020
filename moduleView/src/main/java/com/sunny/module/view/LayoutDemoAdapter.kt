package com.sunny.module.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by zhangxin17 on 2021/1/14
 */
class LayoutDemoAdapter(private val context: Context, private val list: List<LayoutDemoModel>) : RecyclerView.Adapter<LayoutDemoAdapter.ViewHolder>() {

    private var mItemClickListener: DemoItemClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView: View = view
        val iconView: ImageView = view.findViewById(R.id.iconView)
        val nameView: TextView = view.findViewById(R.id.nameView)

        init {
            rootView.setOnClickListener(clickListener)
        }
    }

    fun setItemClickListener(listener: DemoItemClickListener?) {
        this.mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getItem(position: Int): LayoutDemoModel? {
        if (position >= 0 && position < list.size) {
            return list[position]
        }
        return null
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.iconView.setImageResource(R.drawable.ic_gift)
        holder.nameView.text = item.name
        holder.rootView.tag = position
    }

    private val clickListener = View.OnClickListener {

        val pos = it.tag as Int
        val item = getItem(pos)
        if (item != null) {
            mItemClickListener?.onItemClick(item)
        }
    }

}