package com.sunny.lib.utils

import android.content.Context
import android.content.res.Resources

/**
 * Created by zhangxin17 on 2020-01-16
 */
object ResUtils {

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


}