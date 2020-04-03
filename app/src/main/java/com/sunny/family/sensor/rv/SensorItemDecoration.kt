package com.sunny.family.sensor.rv

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by zhangxin17 on 2020/4/3
 */
class SensorItemDecoration(var space: Int = 10) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = space
        outRect.bottom = space
        outRect.left = space
        outRect.right = space
    }
}