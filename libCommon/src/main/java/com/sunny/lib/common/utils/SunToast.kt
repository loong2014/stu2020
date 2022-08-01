package com.sunny.lib.common.utils

import android.widget.Toast
import com.sunny.lib.base.utils.ContextProvider

/**
 * Created by zhangxin17 on 2020-01-10
 */

object SunToast {

    @JvmStatic
    fun show(msg: String?) {
        msg ?: return
        show(msg, Toast.LENGTH_SHORT)
    }

    @JvmStatic
    fun show(msg: String?, duration: Int) {
        msg ?: return
        Toast.makeText(ContextProvider.appContext, msg, duration).show()
    }


}


/**
 * 给String类型添加一个showToast的扩展函数
 */
fun String.showToast() {
    SunToast.show(this)
}