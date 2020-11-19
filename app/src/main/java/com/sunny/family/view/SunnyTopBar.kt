package com.sunny.family.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunny.family.R
import com.sunny.lib.utils.ResUtils
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.layout_top_bar.view.*

/**
 * Created by zhangxin17 on 2020/11/12
 */
class SunnyTopBar(context: Context, attributeSet: AttributeSet?) : ConstraintLayout(context, attributeSet) {

    companion object {
        const val TAG = "SunnyTopBar"
    }

    init {
        SunLog.i(TAG, "init")
        LayoutInflater.from(context).inflate(R.layout.layout_top_bar, this)
        setBackgroundColor(ResUtils.getColor(R.color.white))

        // 默认点击返回处理
        top_bar_left_back_btn.setOnClickListener {
            val act = context as Activity
            act.finish()
        }
    }

    /**
     * 覆盖点击返回处理
     */
    fun setOnBackBtnClickListener(listener: OnClickListener) {
        top_bar_left_back_btn.setOnClickListener(listener)
    }

    fun setMiddleName(name: String) {
        top_bar_mid_name.text = name
    }

    fun getMiddleNameView(): View {
        return top_bar_mid_name
    }

    fun setLogoIcon(@DrawableRes resId: Int) {
        top_bar_right_logo.setImageResource(resId)
    }

    fun setOnLogoIconClickListener(listener: OnClickListener) {
        top_bar_right_logo.setOnClickListener(listener)
    }

    fun getLogoView(): View {
        return top_bar_right_logo
    }

}

