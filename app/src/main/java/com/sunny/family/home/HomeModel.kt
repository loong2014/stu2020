package com.sunny.family.home

import android.graphics.Rect
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunny.family.R
import com.sunny.lib.utils.ResUtils

/**
 * Created by zhangxin17 on 2020/11/13
 */
data class HomeItemModel(var showName: String, var routePath: String)

data class HomeFragmentModel(var name: String, var id: String, var fragment: Fragment)


class HomeItemDecoration : RecyclerView.ItemDecoration {

    private val offset: Int = ResUtils.getDimensionPixelSize(R.dimen.dp_10)

    constructor()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = offset
        outRect.top = offset
        outRect.right = offset
        outRect.bottom = offset
    }
}