package com.sunny.family.list.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.family.list.ListDataModel
import com.sunny.family.list.ListLayoutConstants
import com.sunny.lib.base.BaseFragment
import com.sunny.lib.utils.ResUtils

class RecyclerViewFragment : BaseFragment() {

    lateinit var mRecyclerView: RecyclerView

    lateinit var mAdapter: RecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootLayout = inflater.inflate(R.layout.fragment_list_recycleriew, container, false)
        initView(rootLayout)
        return rootLayout
    }

    private fun initView(root: View) {
        //
        mRecyclerView = root.findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecyclerItemDecoration())
        //
        mAdapter = RecyclerViewAdapter()
        mRecyclerView.adapter = mAdapter

        mAdapter.setNewData(ListLayoutConstants.buildListData())
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconView: AppCompatImageView = itemView.findViewById(R.id.item_icon)
        private val titleView: AppCompatTextView = itemView.findViewById(R.id.item_title)

        fun onBindViewData(data: ListDataModel?) {
            data?.let {
                iconView.setImageResource(it.iconId)
                titleView.text = it.title
            }
        }
    }

    inner class RecyclerItemDecoration : RecyclerView.ItemDecoration() {
        val top = ResUtils.getDimension(R.dimen.dp_10).toInt()
        val bottom = ResUtils.getDimension(R.dimen.dp_10).toInt()
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.top = top
            outRect.bottom = bottom
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

        private var datas: List<ListDataModel>? = null

        fun setNewData(newDatas: List<ListDataModel>) {
            datas = newDatas
        }

        fun getData(position: Int): ListDataModel? {
            return datas?.get(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_item_view_list_data, parent, false)
            return RecyclerViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.onBindViewData(getData(position))
        }

        override fun getItemCount(): Int {
            return datas?.size ?: 0
        }

    }
}