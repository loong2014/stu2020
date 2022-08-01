package com.sunny.lib.common.dialog

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

object DialogBarHelper {
    /**
     * Sets Window status bar light mode
     *
     * @param window               Window
     * @param isStatusBarLightMode Whether status bar font dark
     */
    @JvmStatic
    fun setStatusBarLightMode(window: Window?, isStatusBarLightMode: Boolean) {
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var flags = decorView.systemUiVisibility
            flags = if (isStatusBarLightMode) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = flags
        }
    }

    /**
     * Sets Window status bar transparent
     *
     * @param window Window
     */
    @JvmStatic
    fun setTransparentStatusBar(window: Window?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || window == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val decor = window.decorView
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val flags = (window.decorView.systemUiVisibility
                        and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                decor.systemUiVisibility = option or flags
            } else {
                decor.systemUiVisibility = option
            }
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    @JvmStatic
    fun fitNotch(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        val attributes = window.attributes
        attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = attributes
    }
}