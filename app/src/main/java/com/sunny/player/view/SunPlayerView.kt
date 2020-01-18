package com.sunny.player.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class SunPlayerView : RelativeLayout {

    constructor(context: Context) : super(context) {
//        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        initView(context)
    }

    init {
        isChildrenDrawingOrderEnabled = true
//        LayoutInflater.from(context).inflate(R.layout.player_root_view, this)
    }

    companion object {
        fun buildPlayerView(context: Context): SunPlayerView {
            val view = SunPlayerView(context)
            view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            return view
        }
    }

    fun addVideoView(view: View) {
        addView(view)
    }

}