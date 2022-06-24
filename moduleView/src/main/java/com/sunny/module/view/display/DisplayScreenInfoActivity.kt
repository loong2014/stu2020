package com.sunny.module.view.display

import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.lib.common.base.BaseActivity
import com.sunny.lib.common.router.RouterConstant
import com.sunny.module.view.R
import com.sunny.module.view.databinding.ViewActScreenInfoBinding

@Route(path = RouterConstant.View.PageToolsScreenInfo)
class DisplayScreenInfoActivity : BaseActivity() {

    lateinit var mActBinding: ViewActScreenInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActBinding = DataBindingUtil.setContentView(this, R.layout.view_act_screen_info)
        mActBinding.btnScreenInfo.setOnClickListener {
            doGetScreenInfo()
        }
    }

    private fun doGetScreenInfo() {
        val sb = StringBuilder()

        val wm = windowManager
        val display = wm.defaultDisplay

        sb.append("\n1、display")
        sb.append("\n\tgetWidth(${display.width}) , getHeight(${display.height})")

        val point = Point()
        display.getSize(point)
        sb.append("\n2、display.getSize")
        sb.append("\n\tx(${point.x}) , y(${point.y})")

        var metrics = DisplayMetrics()
        display.getMetrics(metrics)
        sb.append("\n3、display.getMetrics")
        sb.append("\n\tdensity(${metrics.density}) , densityDpi(${metrics.densityDpi}) , widthPixels(${metrics.widthPixels}) , heightPixels(${metrics.heightPixels})")

        metrics = resources.displayMetrics
        sb.append("\n4、resources.displayMetrics")
        sb.append("\n\tdensity(${metrics.density}) , densityDpi(${metrics.densityDpi}) , widthPixels(${metrics.widthPixels}) , heightPixels(${metrics.heightPixels})")

        mActBinding.tvScreenInfo.text = sb.toString()
    }
}