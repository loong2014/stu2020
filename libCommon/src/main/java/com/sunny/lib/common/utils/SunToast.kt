package com.sunny.lib.common.utils

import android.widget.Toast
import com.sunny.lib.utils.ContextProvider

/**
 * Created by zhangxin17 on 2020-01-10
 */
object SunToast {

    @JvmStatic
    fun show(msg: String) {
        show(msg, Toast.LENGTH_SHORT)
    }

    @JvmStatic
    fun show(msg: String, duration: Int) {
        Toast.makeText(ContextProvider.appContext, msg, duration).show()
    }

}