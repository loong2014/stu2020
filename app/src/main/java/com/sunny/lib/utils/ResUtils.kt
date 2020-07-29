package com.sunny.lib.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import kotlin.math.roundToInt

/**
 * Created by zhangxin17 on 2020-01-16
 */
object ResUtils {

    // 基础像素密度density为1.5， 宽1920， 高1080
    private const val BASE_WIDTH = 1920.0f
    private const val BASE_HEIGHT = 1080.0f
    private const val BASE_DENSITY = 1.5f

    private const val mCurrentWidth = 1920.0f
    private const val mCurrentHeight = 1080.0f
    private const val mCurrentDensity = 1.5f

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun getResources(): Resources {
        return context.resources
    }


    fun getDimensionPixelSize(resId: Int): Int {
        return getResources().getDimensionPixelSize(resId)
    }

    fun getString(resId: Int): String {
        return getResources().getString(resId)
    }

    fun getString(resId: Int, vararg formatArgs: Any?): String? {
        return getResources().getString(resId, *formatArgs)
    }

    fun getColor(resId: Int): Int {
        return getResources().getColor(resId)
    }

    fun getDrawable(resId: Int): Drawable? {
        return getResources().getDrawable(resId)
    }

    fun getBitmap(resId: Int): Bitmap {
        return BitmapFactory.decodeResource(getResources(), resId)
    }


    fun scaleWidth(pixel: Int): Int {
        return (adpaterDensity(pixel.toFloat()) * mCurrentWidth / BASE_WIDTH).roundToInt()
    }

    fun scaleHeight(pixel: Int): Int {
        return (this.adpaterDensity(pixel.toFloat()) * mCurrentHeight / BASE_HEIGHT).roundToInt()
    }

    private fun adpaterDensity(pixel: Float): Int {
        return (0.5f + pixel / mCurrentDensity * BASE_DENSITY) as Int
    }


}