package com.sunny.family.list.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import com.sunny.family.R
import com.sunny.family.extend.lettersCount
import com.sunny.family.list.ListDataModel
import com.sunny.family.list.ListLayoutConstants
import com.sunny.family.list.ListViewHolder
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.utils.SunToast

class ListViewFragment : BaseFragment() {

    lateinit var mListView: ListView
    lateinit var mAdapter: ListViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootLayout = inflater.inflate(R.layout.fragment_list_listview, container, false)
        initView(rootLayout)
        return rootLayout
    }

    private fun initView(root: View) {

        // 判断mListView是否已经初始化
        if (::mListView.isInitialized) {
            return
        }

        mListView = root.findViewById(R.id.list_view)

        /*
        val data = listOf<String>("aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg")
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, data)
        mListView.adapter = adapter
        */
        mAdapter = ListViewAdapter(context!!)
        mAdapter.setNewData(ListLayoutConstants.buildListData())

        mListView.adapter = mAdapter

        //
        mListView.setOnItemClickListener { _, _, position, _ ->
            mAdapter.getItem(position)?.let {
                SunToast.show(it.title)
            }
        }
    }

    inner class ListViewAdapter(val context: Context, var datas: List<ListDataModel>? = null) : BaseAdapter() {

        var inflater: LayoutInflater = LayoutInflater.from(context)

        fun setNewData(newDatas: List<ListDataModel>) {
            this.datas = newDatas
        }

        override fun getCount(): Int {
            return datas?.size ?: 0
        }

        override fun getItem(position: Int): ListDataModel? {
            return datas?.run {
                get(position)
            }
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val itemView: View
            val holder: ListViewHolder

            if (convertView == null) {
                holder = ListViewHolder()

                // TODO: 2020/11/26  inflate时，parent 为什么不能直接传null？
                itemView = inflater.inflate(R.layout.layout_item_view_list_data, parent, false)

                holder.iconView = itemView.findViewById(R.id.item_icon)
                holder.titleView = itemView.findViewById(R.id.item_title)

                itemView.tag = holder
            } else {

                itemView = convertView
                holder = itemView.tag as ListViewHolder
            }

            //
            getItem(position)?.let {
                holder.apply {
                    iconView?.setImageResource(it.iconId)
                    titleView?.text = it.title
                }
            }

            return itemView
        }
    }
}