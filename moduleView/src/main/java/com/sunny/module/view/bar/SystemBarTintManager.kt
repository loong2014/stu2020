package com.sunny.module.view.bar

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.sunny.lib.common.utils.ResUtils


/**
 * 系统状态栏着色器
 * 1. Android4.4（API19 KitKat）以前：无法做任何事，是的，就是一坨黑色。
 * 2. Android4.4～Android5.0（API21 Lollipop）：可以实现状态栏的变色，但是效果还不是很好，
 * 主要实现方式是通过FLAG_TRANSLUCENT_STATUS这个属性设置状态栏为透明并且为全屏模式，
 * 然后通过添加一个与StatusBar 一样大小的View，将View 设置为我们想要的颜色，从而来实现状态栏变色。
 * 3. Android5.0～Android6.0（API23 Marshmallow）: 系统才真正的支持状态栏变色，
 * 系统加入了一个重要的属性和方法 android:statusBarColor （对应方法为 setStatusBarColor），通过这个属性可以直接设置状态栏的颜色
 * 4. Android6.0以后：主要就是添加了一个功能可以修改状态栏上内容和图标的颜色（黑色和白色）
 */
object SystemBarTintManager {

    /**
     * 全屏，不显示状态栏
     */
    fun fullscreen(activity: Activity) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    /**
     * 透明状态栏
     * 等同与在them中添加<item name="android:windowTranslucentStatus">true</item>
     * 4.4以下无透明效果
     * 4.4~5.0是全透明
     * 5.0以上是半透明
     *
     * 结合根布局设置了android:fitsSystemWindows属性
     * 1. fitsSystemWindows 生效前提：当前页面没有标题栏，并且状态栏或者底部导航栏透明
     * 2. fitsSystemWindows = true，表示内容区不延伸到状态栏或底部导航栏
     * 3. fitsSystemWindows = false，表示内容区延伸到状态栏或底部导航栏
     */
    fun translucentStatus(activity: Activity) {

        // >= 4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // 半透明
//            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // 全透明
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

            // 设置状态栏颜色
            activity.window.statusBarColor = Color.TRANSPARENT
//            activity.window.statusBarColor = ResUtils.getColor(R.color.colorAccent);
        }
    }

    /**
     * 设置顶部view的padding为状态栏的高度
     */
    fun setViewPaddingTopStatusBar(activity: Activity, view: View) {
        val statusBarHeight = getStatusHeight()
        view.setPadding(0,
                statusBarHeight,
                0,
                0)
    }

    /**
     * 设置状态栏文案字体颜色
     */
    fun setStatusContentColor(activity: Activity, dark: Boolean) {
        if (dark) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    /**
     * 获取状态栏的高度——手机状态栏的高度
     * 可行——推荐
     */
    private fun getStatusHeight(): Int {
        //获取status_bar_height资源的ID
        val resourceId: Int = ResUtils.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return ResUtils.getDimensionPixelSize(resourceId)
        }
        return 0
    }

    /**
     * 获取状态栏的高度——通过反射
     * 可行
     */
    private fun getStatusHeight2(): Int {

        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val obj = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[obj].toString().toInt()

            return ResUtils.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }


    /**
     * 获取状态栏的高度--状态栏高度 = 屏幕高度 - 应用区高度
     * 无效：当设置fitsSystemWindows = false时，获取到的高度=0
     */
    private fun getStatusHeight3(activity: Activity): Int {
        //屏幕
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)

        //应用区域
        val contentRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(contentRect)

        //状态栏高度=屏幕高度-应用区域高度
        return dm.heightPixels - contentRect.height()
    }

}