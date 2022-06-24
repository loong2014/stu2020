package com.sunny.lib.base.utils

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import com.sunny.lib.base.R

object FontUtils {

    fun getFont(): Int {
        return R.font.ma_shan_zheng_regular
//        return R.font.pax_rubik_debug
    }

    @JvmStatic
    fun changeDefaultFont(view: TextView) {
//        changeDefaultFont(view, getFont())
    }


    /**
     * 遍历布局的 ViewTree, 找到 TextView 及其子类进行批量替换
     */
    @JvmStatic
    fun changeDefaultFont(view: View?, toFont: Int) {
        if (view != null && toFont > 0) {
            when (view) {
                is ViewGroup -> {
                    view.forEach {
                        changeDefaultFont(it, toFont)
                    }
                }
                is TextView -> {
                    try {
                        val typeface = ResourcesCompat.getFont(view.context, toFont)
                        val fontStyle = view.typeface?.style ?: Typeface.NORMAL
                        view.setTypeface(typeface, fontStyle)
//                        view.setBackgroundColor(Color.GREEN)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}