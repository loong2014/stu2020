package com.sunny.lib.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import androidx.palette.graphics.Palette

/**
 * Created by zhangxin17 on 2020-01-19
 */
object BackgroundUtils {

    /**
     * 根据图片网络地址获取背景的色值列表
     */
    fun getBackgroundColor(imgUrl: String, callback: IBackgroundColorCallback) {

        BitmapUtils.getBitmap(imgUrl, object : BitmapUtils.IBitmapCallback {
            override fun onGetBitmap(bitmap: Bitmap?) {
                if (bitmap == null) {
                    callback.onGetBackgroundColorError()

                } else {
                    getBackgroundColor(bitmap, callback)
                }
            }
        })
    }

    /**
     * 根据图片获取背景的色值列表
     */
    fun getBackgroundColor(bitmap: Bitmap, callback: IBackgroundColorCallback) {

        Palette.from(bitmap).maximumColorCount(32).generate { palette ->
            val bgColor = palette?.let {
                when {
                    palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                        palette.getDarkMutedColor(Color.TRANSPARENT)
                    }

                    palette.getMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                        palette.getMutedColor(Color.TRANSPARENT)
                    }

                    palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                        palette.getDarkVibrantColor(Color.TRANSPARENT)
                    }

                    else -> {
                        Color.BLACK
                    }
                }
            } ?: Color.BLACK

            callback.onGetBackgroundColor(intArrayOf(colorTransNew(bgColor, 50.0), colorTransNew(bgColor, 120.0)))
        }
    }

    /**
     * 更新背景
     *
     * @param view   需要更新的view
     * @param colors 色值列表
     */

    fun updateBackground(view: View, colors: IntArray) {
        val shapeDrawable = ShapeDrawable(RectShape())

        shapeDrawable.paint.shader =
                LinearGradient(0F, 0F, 1F, view.height.toFloat(),
                        colors, null,
                        Shader.TileMode.REPEAT)
        view.setBackgroundDrawable(shapeDrawable)
    }

    private fun colorTransNew(ARGBValues: Int, y: Double): Int {
        val yuv = palettecolor2yuv(ARGBValues)
        yuv[1] = y
        return yuv2argb(yuv[1], yuv[2], yuv[3], yuv[0])
    }

    private fun palettecolor2yuv(argbValues: Int): DoubleArray {
        val argb = palettecolor2rgb(argbValues)
        return argb2yuv(argb[0], argb[1], argb[2], argb[3])
    }

    private fun palettecolor2rgb(argbValues: Int): IntArray {
        val argb = IntArray(4)
        argb[0] = argbValues shr 24
        argb[1] = argbValues shr 16 and 0xFF
        argb[2] = argbValues shr 8 and 0xFF
        argb[3] = argbValues and 0xFF
        return argb
    }

    private fun argb2yuv(A: Int, R: Int, G: Int, B: Int): DoubleArray {
        val yuv = DoubleArray(4)
        yuv[0] = A.toDouble()
        yuv[1] = 0.299 * R + 0.587 * G + 0.114 * B
        yuv[2] = -0.147 * R - 0.289 * G + 0.437 * B
        yuv[3] = 0.615 * R - 0.515 * G - 0.1 * B
        return yuv
    }

    private fun yuv2argb(y: Double, u: Double, v: Double, A: Double): Int {
        var R = (y + 1.14 * v).toInt()
        var G = (y - 0.394 * u - 0.581 * v).toInt()
        var B = (y + 2.028 * u).toInt()
        if (R < 0) R = 0
        if (G < 0) G = 0
        if (B < 0) B = 0
        if (R > 255) R = 255
        if (G > 255) G = 255
        if (B > 255) B = 255
        return Color.argb(A.toInt(), R, G, B)
    }

    interface IBackgroundColorCallback {
        fun onGetBackgroundColor(colors: IntArray)
        fun onGetBackgroundColorError()
    }
}