package com.sunny.family.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunny.family.R
import com.sunny.lib.utils.SunLog
import kotlinx.android.synthetic.main.layout_top_bar.view.*

/**
 * Created by zhangxin17 on 2020/11/12
 */
class SunnyTopBar : FrameLayout {

    companion object {
        const val TAG = "SunnyTopBar"
    }

    init {
        SunLog.i(TAG, "init")
    }

    private lateinit var rootLayout: ConstraintLayout

    constructor(context: Context) : super(context) {
        SunLog.i(TAG, "constructor 1")
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        SunLog.i(TAG, "constructor 2")
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        SunLog.i(TAG, "constructor 3")
        initView(context)
    }

    private fun initView(context: Context) {
        SunLog.i(TAG, "initView")
        rootLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_top_bar, null) as ConstraintLayout
        addView(rootLayout)
    }

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

