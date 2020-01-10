package com.sunny.family

import android.content.Context
import com.sunny.lib.utils.FileUtils

/**
 * Created by zhangxin17 on 2020-01-10
 */
object AppConfig {

    fun init(context: Context) {

        FileUtils.init(context)
    }

}