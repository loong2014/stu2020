package com.sunny.lib.utils

import android.widget.Toast

/**
 * Created by zhangxin17 on 2020-01-10
 */
object ToastUtils {

    fun show(msg: String) {
        show(msg, Toast.LENGTH_SHORT)
    }

    fun show(msg: String, duration: Int) {
        Toast.makeText(ContextProvider.appContext, msg, duration).show()
    }
}