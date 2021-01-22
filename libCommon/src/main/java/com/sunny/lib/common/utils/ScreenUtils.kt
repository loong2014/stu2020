package com.sunny.lib.common.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.sunny.lib.utils.ContextProvider
import com.sunny.lib.base.log.SunLog


object ScreenUtils {

    val screenWidth: Int

    val screenHeight: Int


    init {

        //获取windowManager
        val windowManager = ContextProvider.appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        //获取屏幕对象
        val defaultDisplay = windowManager.defaultDisplay

        val outPoint = Point()
        defaultDisplay.getSize(outPoint)

        //获取屏幕的宽、高，单位是像素
        screenWidth = outPoint.x
        screenHeight = outPoint.y

        SunLog.i("ScreenUtils", "screenWidth :$screenWidth , screenHeight :$screenHeight")
    }
}