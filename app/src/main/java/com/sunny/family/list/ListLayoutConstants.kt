package com.sunny.family.list

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.sunny.family.R

object ListLayoutConstants {
    const val FragmentCount = 2 // fragment个数
    const val FragmentListView = 0
    const val FragmentRecyclerView = 1

    @JvmStatic
    fun buildListData(): List<ListDataModel> {
        return mutableListOf<ListDataModel>().apply {
            for (i in 0..50) {
                add(ListDataModel(title = "name${-i}"))
            }
        }
    }
}

data class ListDataModel(val title: String, val iconId: Int = R.drawable.icon_gift)

data class ListViewHolder(var titleView: AppCompatTextView? = null, var iconView: AppCompatImageView? = null)
