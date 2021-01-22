package com.sunny.lib.common.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar


class SunSnackBar {

    fun showSnackbar(view: View, text: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, text, duration).show()
    }


}


/**
 * 给View类型添加一个扩展函数
 */
fun View.showSnckbar(text: String,
                     actionText: String? = null,
                     duration: Int = Snackbar.LENGTH_SHORT,
                     block: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, text, duration)
    if (actionText != null && block != null) {
        snackbar.setAction(actionText) {
            block()
        }
    }
    snackbar.show()
}