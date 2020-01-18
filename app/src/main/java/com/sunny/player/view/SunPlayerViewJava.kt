package com.sunny.player.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class SunPlayerViewJava : RelativeLayout {
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {}
    fun addVideoView(view: View?) {
        addView(view)
    }
}